package com.example.stocksync;

import com.example.stocksync.entity.Product;
import com.example.stocksync.service.VendorBClient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VendorBClientTest {

    @Test
    void fetchProducts_shouldReturnProductsFromCSV() {
        VendorBClient client = new VendorBClient();
        List<Product> products = client.fetchProducts();

        assertNotNull(products);
        assertEquals(2, products.size());

        Product first = products.get(0);
        assertEquals("ABC123", first.getSku());
        assertEquals("Product A", first.getName());
        assertEquals(10, first.getStockQuantity());
        assertEquals("VendorB", first.getVendor());
    }
}
