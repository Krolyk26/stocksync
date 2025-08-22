package com.example.stocksync.service;

import com.example.stocksync.dto.ProductDTO;
import com.example.stocksync.dto.VendorAProductDTO;
import com.example.stocksync.dto.VendorBProductDTO;
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

    @Scheduled(fixedRateString = "${sync.interval.ms}")
    public void syncStock() {
        log.info("Starting stock sync...");

        List<VendorAProductDTO> vendorAProducts = vendorAClient.fetchProducts();
        for (VendorAProductDTO dto : vendorAProducts) {
            ProductDTO productDTO = new ProductDTO(dto.sku(), dto.name(), dto.stockQuantity(), dto.vendor());
            upsertProduct(productDTO);
        }

        List<VendorBProductDTO> vendorBProducts = vendorBClient.fetchProducts();
        for (VendorBProductDTO dto : vendorBProducts) {
            ProductDTO productDTO = new ProductDTO(dto.sku(), dto.name(), dto.stockQuantity(), dto.vendor());
            upsertProduct(productDTO);
        }

        log.info("Stock sync completed.");
    }


    public void upsertProduct(ProductDTO productDTO) {
        Product product = productRepository.findBySkuAndVendor(productDTO.sku(), productDTO.vendor())
                .orElse(new Product(
                        productDTO.sku(),
                        productDTO.name(),
                        productDTO.stockQuantity(),
                        productDTO.vendor()
                ));

        boolean isNewProduct = product.getId() == null;
        Integer oldStockQuantity = product.getStockQuantity();

        if (!isNewProduct) {
            product.setName(productDTO.name());
            product.setStockQuantity(productDTO.stockQuantity());
        }

        if ((!isNewProduct && oldStockQuantity != null && oldStockQuantity > 0 && productDTO.stockQuantity() == 0) ||
                (isNewProduct && productDTO.stockQuantity() == 0)) {
            log.warn("Product {} ({}) is now OUT OF STOCK!", productDTO.sku(), productDTO.vendor());
            StockEvent event = new StockEvent(productDTO.sku(), productDTO.vendor(), LocalDateTime.now());
            stockEventRepository.save(event);
        }

        productRepository.save(product);
    }
}