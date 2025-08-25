package com.example.stocksync;

import com.example.stocksync.entity.product.Product;
import com.example.stocksync.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.devtools.restart.enabled=false"
})
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

    @Test
    void uniqueConstraint_onSkuAndVendor_preventsDuplicates() {
        Product p1 = new Product("SKU-UNIQ", "Name1", 1, "Vendor U");
        productRepository.saveAndFlush(p1);

        Product p2 = new Product("SKU-UNIQ", "Name2", 2, "Vendor U");
        try {
            productRepository.saveAndFlush(p2);
            assertTrue(false, "Expected unique constraint violation");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}