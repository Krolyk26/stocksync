package com.example.stocksync;

import com.example.stocksync.dto.ProductDTO;
import com.example.stocksync.dto.VendorAProductDTO;
import com.example.stocksync.dto.VendorBProductDTO;
import com.example.stocksync.entity.Product;
import com.example.stocksync.repository.ProductRepository;
import com.example.stocksync.repository.StockEventRepository;
import com.example.stocksync.service.StockSyncService;
import com.example.stocksync.service.VendorAClient;
import com.example.stocksync.service.VendorBClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockSyncServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockEventRepository stockEventRepository;

    @Mock
    private VendorAClient vendorAClient;

    @Mock
    private VendorBClient vendorBClient;

    @InjectMocks
    private StockSyncService stockSyncService;

    private Product existingProduct;

    @BeforeEach
    void setUp() {
        existingProduct = new Product("SKU1", "Existing Product", 10, "VendorA");
        existingProduct.setId(1L);
    }

    @Test
    void syncStock_shouldFetchAndProcessAllVendorProducts() {
        List<VendorAProductDTO> vendorAProducts = Arrays.asList(
                new VendorAProductDTO("A-1", "Product from A", 50, "VendorA"),
                new VendorAProductDTO("A-2", "Product from A out", 0, "VendorA")
        );

        List<VendorBProductDTO> vendorBProducts = Arrays.asList(
                new VendorBProductDTO("B-1", "Product from B", 25, "VendorB")
        );

        when(vendorAClient.fetchProducts()).thenReturn(vendorAProducts);
        when(vendorBClient.fetchProducts()).thenReturn(vendorBProducts);

        when(productRepository.findBySkuAndVendor(anyString(), anyString()))
                .thenReturn(Optional.empty());

        stockSyncService.syncStock();

        verify(vendorAClient, times(1)).fetchProducts();
        verify(vendorBClient, times(1)).fetchProducts();

        verify(productRepository, times(3)).save(any(Product.class));

        verify(stockEventRepository, times(1)).save(any());
    }

    @Test
    void upsertProduct_shouldUpdateExistingProduct_whenFound() {
        ProductDTO productDTO = new ProductDTO("SKU1", "Updated Product", 5, "VendorA");
        when(productRepository.findBySkuAndVendor("SKU1", "VendorA"))
                .thenReturn(Optional.of(existingProduct));

        stockSyncService.upsertProduct(productDTO);

        verify(productRepository, times(1)).save(any(Product.class));
        assertEquals("Updated Product", existingProduct.getName());
        assertEquals(5, existingProduct.getStockQuantity());
    }

    @Test
    void upsertProduct_shouldCreateNewProduct_whenNotFound() {
        ProductDTO productDTO = new ProductDTO("SKU2", "New Product", 20, "VendorB");
        when(productRepository.findBySkuAndVendor("SKU2", "VendorB"))
                .thenReturn(Optional.empty());

        stockSyncService.upsertProduct(productDTO);

        verify(productRepository, times(1)).save(any(Product.class));
        verify(stockEventRepository, never()).save(any());
    }

    @Test
    void upsertProduct_shouldLogStockEvent_whenGoesOutOfStock() {
        ProductDTO productDTO = new ProductDTO("SKU1", "Existing Product", 0, "VendorA");
        when(productRepository.findBySkuAndVendor("SKU1", "VendorA"))
                .thenReturn(Optional.of(existingProduct));
        stockSyncService.upsertProduct(productDTO);
        verify(stockEventRepository, times(1)).save(any());
    }
}