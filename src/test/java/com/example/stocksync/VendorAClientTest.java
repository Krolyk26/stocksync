package com.example.stocksync;

import com.example.stocksync.dto.VendorAProductDTO;
import com.example.stocksync.service.VendorAClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendorAClientTest {

    @Mock
    private RestTemplate restTemplate;

    private VendorAClient vendorAClient;
    private final String url = "http://mock-url/vendor-a";

    @BeforeEach
    void setUp() {
        vendorAClient = new VendorAClient(restTemplate, url);
    }

    @Test
    void fetchProducts_shouldReturnListOfProducts_fromRestAPI() {
        VendorAProductDTO[] mockProducts = {
                new VendorAProductDTO("SKU1", "Product A", 10, "Vendor A"),
                new VendorAProductDTO("SKU2", "Product B", 0, "Vendor A")
        };

        when(restTemplate.getForObject(url, VendorAProductDTO[].class)).thenReturn(mockProducts);

        List<VendorAProductDTO> products = vendorAClient.fetchProducts();

        assertEquals(2, products.size());
        assertEquals("SKU1", products.get(0).sku());
    }
}