package com.example.stocksync.controller;

import com.example.stocksync.entity.product.dto.ProductResponse;
import com.example.stocksync.service.productService.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("GET /products requested");
        List<ProductResponse> products = productService.getAllProducts();
        log.info("GET /products returned {} items", products.size());
        return ResponseEntity.ok(products);
    }
}
