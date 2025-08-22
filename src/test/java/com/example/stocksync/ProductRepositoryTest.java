package com.example.stocksync;

import com.example.stocksync.entity.Product;
import com.example.stocksync.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findBySkuAndVendor_shouldReturnProduct_whenFound() {
        Product product = new Product("SKU123", "Test Product", 5, "TestVendor");
        productRepository.save(product);

        var foundProduct = productRepository.findBySkuAndVendor("SKU123", "TestVendor");

        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());
    }

    @Test
    void findBySkuAndVendor_shouldReturnEmptyOptional_whenNotFound() {
        var foundProduct = productRepository.findBySkuAndVendor("NON_EXISTENT", "TestVendor");

        assertTrue(foundProduct.isEmpty());
    }
}