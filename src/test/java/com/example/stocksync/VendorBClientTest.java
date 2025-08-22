package com.example.stocksync;

import com.example.stocksync.dto.VendorBProductDTO;
import com.example.stocksync.service.VendorBClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class VendorBClientTest {

    @InjectMocks
    private VendorBClient vendorBClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(vendorBClient, "csvFilePath", "test-vendor-b-stock.csv");
    }

    @Test
    void fetchProducts_shouldParseCsvFileCorrectly() {
        List<VendorBProductDTO> products = vendorBClient.fetchProducts();

        assertFalse(products.isEmpty());
        assertEquals(3, products.size());
        assertEquals("SKU101", products.get(0).sku());
        assertEquals(50, products.get(0).stockQuantity());
        assertEquals("SKU103", products.get(2).sku());
        assertEquals(0, products.get(2).stockQuantity());
    }
}