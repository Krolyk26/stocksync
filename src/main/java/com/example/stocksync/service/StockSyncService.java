package com.example.stocksync.service;
import com.example.stocksync.dto.VendorAProductDTO;
import com.example.stocksync.entity.Product;
import com.example.stocksync.entity.StockEvent;
import com.example.stocksync.repository.ProductRepository;
import com.example.stocksync.repository.StockEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockSyncService {

    private static final Logger log = LoggerFactory.getLogger(StockSyncService.class);

    private final ProductRepository productRepository;
    private final StockEventRepository stockEventRepository;
    private final VendorAClient vendorAClient;
    private final VendorBClient vendorBClient;

    public StockSyncService(ProductRepository productRepository,
                            StockEventRepository stockEventRepository,
                            VendorAClient vendorAClient,
                            VendorBClient vendorBClient) {
        this.productRepository = productRepository;
        this.stockEventRepository = stockEventRepository;
        this.vendorAClient = vendorAClient;
        this.vendorBClient = vendorBClient;
    }

    @Scheduled(fixedRate = 60000)
    public void syncStock() {
        log.info("Starting stock sync...");

        List<VendorAProductDTO> vendorAProducts = vendorAClient.fetchProducts();
        for (VendorAProductDTO dto : vendorAProducts) {
            upsertProduct(dto.sku(), dto.name(), dto.stockQuantity(), "VendorA");
        }

        List<Product> vendorBProducts = vendorBClient.fetchProducts();
        for (Product p : vendorBProducts) {
            upsertProduct(p.getSku(), p.getName(), p.getStockQuantity(), p.getVendor());
        }

        log.info("Stock sync completed.");
    }

    private void upsertProduct(String sku, String name, Integer stockQuantity, String vendor) {
        Optional<Product> optionalProduct = productRepository.findBySkuAndVendor(sku, vendor);

        if (optionalProduct.isPresent()) {
            Product existing = optionalProduct.get();

            if (existing.getStockQuantity() > 0 && stockQuantity == 0) {
                log.warn("Product {} ({}) is now OUT OF STOCK!", sku, vendor);
                StockEvent event = new StockEvent(sku, vendor, LocalDateTime.now());
                stockEventRepository.save(event);
            }

            existing.setName(name);
            existing.setStockQuantity(stockQuantity);
            productRepository.save(existing);

        } else {
            Product newProduct = new Product();
            newProduct.setSku(sku);
            newProduct.setName(name);
            newProduct.setStockQuantity(stockQuantity);
            newProduct.setVendor(vendor);
            productRepository.save(newProduct);

            if (stockQuantity == 0) {
                log.warn("New product {} ({}) is OUT OF STOCK!", sku, vendor);
                StockEvent event = new StockEvent(sku, vendor, LocalDateTime.now());
                stockEventRepository.save(event);
            }
        }
    }
}
