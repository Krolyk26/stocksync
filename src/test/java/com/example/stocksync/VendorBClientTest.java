package com.example.stocksync;

import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import com.example.stocksync.service.vendor.VendorBClient;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendorBClientTest {

    @Test
    void fetchProducts_shouldParseCsvFileCorrectly() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        VendorBClient client = new VendorBClient(resourceLoader);
        setField(client, "csvLocation", "classpath:test-vendor-b-stock.csv");

        List<ProductCreateRequest> products = client.fetchProducts();

        assertNotNull(products);
        assertEquals(3, products.size());
        assertEquals("SKU101", products.get(0).sku());
        assertEquals(0, products.get(0).stockQuantity());
        assertEquals("SKU103", products.get(2).sku());
        assertEquals(0, products.get(2).stockQuantity());
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}