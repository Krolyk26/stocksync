package com.example.stocksync.service;

import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import com.example.stocksync.entity.product.dto.ProductResponse;
import com.example.stocksync.entity.product.dto.ProductUpdateRequest;
import com.example.stocksync.entity.stockEvent.StockEventStatus;
import com.example.stocksync.entity.stockEvent.dto.StockEventCreateRequest;
import com.example.stocksync.service.productService.ProductService;
import com.example.stocksync.service.stockEventService.StockEventService;
import com.example.stocksync.service.vendor.Vendor;
import com.example.stocksync.service.vendor.VendorAClient;
import com.example.stocksync.service.vendor.VendorBClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockSyncService {
    private static final Logger log = LoggerFactory.getLogger(StockSyncService.class);

    private final ProductService productService;
    private final StockEventService stockEventService;
    private final VendorAClient vendorAClient;
    private final VendorBClient vendorBClient;

    @Scheduled(fixedRateString = "${sync.interval.ms}")
    public void syncStock() {
        log.info("Starting stock sync...");

        List<Vendor> vendors = List.of(vendorAClient, vendorBClient);
        vendors.parallelStream().forEach(this::run);
        log.info("Stock sync completed.");
    }

    private void run(Vendor vendor){
        List<ProductCreateRequest> products = vendor.fetchProducts();

        products.parallelStream().forEach(product -> {
            Optional<ProductResponse> existingProductOptional = productService.findBySkuAndVendor(product.sku(), product.vendor());
            if (existingProductOptional.isEmpty()) {
                productService.createProduct(product);
            }else {
                ProductResponse existingProduct = existingProductOptional.get();
                ProductResponse updatedProduct = productService.updateProduct(existingProduct.id(),
                        new ProductUpdateRequest(product.name(), product.stockQuantity()));

                if (updatedProduct.stockQuantity() == 0 && existingProduct.stockQuantity() > 0){
                    log.warn("Product {} ({}) is now OUT OF STOCK!", updatedProduct.sku(), updatedProduct.vendor());
                    stockEventService.createStockEvent(new StockEventCreateRequest(existingProduct.id(), StockEventStatus.OUT_OF_STOCK));
                } else if (updatedProduct.stockQuantity() > 0 && existingProduct.stockQuantity() == 0){
                    stockEventService.createStockEvent(new StockEventCreateRequest(existingProduct.id(), StockEventStatus.IN_STOCK));
                }

            }
        });
    }
}