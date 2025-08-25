package com.example.stocksync.controller;
import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class VendorAMockController {
    private static final String VENDOR_NAME = "Vendor A";
    private static final Logger log = LoggerFactory.getLogger(VendorAMockController.class);

    @GetMapping("/mock/vendor-a")
    public List<ProductCreateRequest> getVendorAProducts() {
        List<ProductCreateRequest> list = Arrays.asList(
                new ProductCreateRequest("ABC123", "Product A", 8, VENDOR_NAME),
                new ProductCreateRequest("LMN789", "Product C", 2, VENDOR_NAME),
                new ProductCreateRequest("XYZ456", "Product B", 10, VENDOR_NAME),
                new ProductCreateRequest("DEF222", "Product D", 0, VENDOR_NAME),
                new ProductCreateRequest("QWE555", "Product E", 15, VENDOR_NAME)
        );
        log.info("GET /mock/vendor-a returned {} items", list.size());
        return list;
    }
}
