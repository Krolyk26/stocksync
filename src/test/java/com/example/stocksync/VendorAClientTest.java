package com.example.stocksync;

import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import com.example.stocksync.service.vendor.VendorAClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendorAClientTest {

    @Mock
    private RestTemplate restTemplate;

    private VendorAClient vendorAClient;

    @BeforeEach
    void setUp() {
        vendorAClient = new VendorAClient(restTemplate);
    }

    @Test
    void fetchProducts_shouldReturnListOfProducts_fromRestAPI() {
        String url = "http://mock-url/vendor-a";
        setField(vendorAClient, "vendorAUrl", url);

        ProductCreateRequest[] mockProducts = {
                new ProductCreateRequest("SKU1", "Product A", 10, "Vendor A"),
                new ProductCreateRequest("SKU2", "Product B", 0, "Vendor A")
        };

        when(restTemplate.getForObject(url, ProductCreateRequest[].class)).thenReturn(mockProducts);

        List<ProductCreateRequest> products = vendorAClient.fetchProducts();

        assertEquals(2, products.size());
        assertEquals("SKU1", products.get(0).sku());
    }

    @Test
    void fetchProducts_shouldReturnEmpty_whenNullPayload() {
        String url = "http://mock-url/vendor-a";
        setField(vendorAClient, "vendorAUrl", url);
        when(restTemplate.getForObject(url, ProductCreateRequest[].class)).thenReturn(null);

        List<ProductCreateRequest> products = vendorAClient.fetchProducts();
        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    void fetchProducts_filtersOutNullEntries_inPayloadArray() {
        String url = "http://mock-url/vendor-a";
        setField(vendorAClient, "vendorAUrl", url);

        ProductCreateRequest[] mockProducts = new ProductCreateRequest[] {
                null,
                new ProductCreateRequest("SKU3", "Product C", 1, "Vendor A"),
                null
        };
        when(restTemplate.getForObject(url, ProductCreateRequest[].class)).thenReturn(mockProducts);

        List<ProductCreateRequest> products = vendorAClient.fetchProducts();
        assertEquals(1, products.size());
        assertEquals("SKU3", products.get(0).sku());
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