package com.example.stocksync;

import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import com.example.stocksync.entity.product.dto.ProductResponse;
import com.example.stocksync.entity.product.dto.ProductUpdateRequest;
import com.example.stocksync.entity.stockEvent.dto.StockEventCreateRequest;
import com.example.stocksync.service.StockSyncService;
import com.example.stocksync.service.productService.ProductService;
import com.example.stocksync.service.stockEventService.StockEventService;
import com.example.stocksync.service.vendor.VendorAClient;
import com.example.stocksync.service.vendor.VendorBClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockSyncServiceTest {

    @Mock private ProductService productService;
    @Mock private StockEventService stockEventService;
    @Mock private VendorAClient vendorAClient;
    @Mock private VendorBClient vendorBClient;

    @InjectMocks private StockSyncService stockSyncService;

    private ProductResponse existingInStock;

    @BeforeEach
    void setUp() {
        existingInStock = new ProductResponse(1L, "SKU1", "Existing", 5, "Vendor A", null, null);
    }

    @Test
    void syncStock_createsNew_updatesExisting_and_emitsOutOfStock() {
        List<ProductCreateRequest> a = List.of(
                new ProductCreateRequest("SKU1", "Existing", 0, "Vendor A"),
                new ProductCreateRequest("A-2", "New A", 3, "Vendor A")
        );
        List<ProductCreateRequest> b = List.of(
                new ProductCreateRequest("B-1", "New B", 2, "Vendor B")
        );
        when(vendorAClient.fetchProducts()).thenReturn(a);
        when(vendorBClient.fetchProducts()).thenReturn(b);

        when(productService.findBySkuAndVendor("SKU1", "Vendor A")).thenReturn(Optional.of(existingInStock));
        when(productService.findBySkuAndVendor("A-2", "Vendor A")).thenReturn(Optional.empty());
        when(productService.findBySkuAndVendor("B-1", "Vendor B")).thenReturn(Optional.empty());

        when(productService.updateProduct(eq(1L), any(ProductUpdateRequest.class)))
                .thenReturn(new ProductResponse(1L, "SKU1", "Existing", 0, "Vendor A", null, null));

        stockSyncService.syncStock();

        verify(productService, times(3)).findBySkuAndVendor(anyString(), anyString());
        verify(productService, times(2)).createProduct(any(ProductCreateRequest.class));
        verify(productService, times(1)).updateProduct(eq(1L), any(ProductUpdateRequest.class));
        verify(stockEventService, times(1)).createStockEvent(any(StockEventCreateRequest.class));
    }

    @Test
    void syncStock_emitsInStock_whenTransitionFromZeroToPositive() {
        List<ProductCreateRequest> a = List.of(
                new ProductCreateRequest("SKU1", "Existing", 5, "Vendor A")
        );
        when(vendorAClient.fetchProducts()).thenReturn(a);
        when(vendorBClient.fetchProducts()).thenReturn(List.of());

        ProductResponse existingOut = new ProductResponse(2L, "SKU1", "Existing", 0, "Vendor A", null, null);
        when(productService.findBySkuAndVendor("SKU1", "Vendor A")).thenReturn(Optional.of(existingOut));
        when(productService.updateProduct(eq(2L), any(ProductUpdateRequest.class)))
                .thenReturn(new ProductResponse(2L, "SKU1", "Existing", 5, "Vendor A", null, null));

        stockSyncService.syncStock();

        verify(stockEventService, times(1)).createStockEvent(any(StockEventCreateRequest.class));
    }
}