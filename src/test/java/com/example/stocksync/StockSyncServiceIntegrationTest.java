package com.example.stocksync;

import com.example.stocksync.dto.VendorAProductDTO;
import com.example.stocksync.entity.Product;
import com.example.stocksync.entity.StockEvent;
import com.example.stocksync.repository.ProductRepository;
import com.example.stocksync.repository.StockEventRepository;
import com.example.stocksync.service.StockSyncService;
import com.example.stocksync.service.VendorAClient;
import com.example.stocksync.service.VendorBClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(StockSyncServiceIntegrationTest.TestConfig.class)
class StockSyncServiceIntegrationTest {

    @Autowired
    private StockSyncService stockSyncService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockEventRepository stockEventRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private VendorBClient vendorBClient;

    static class TestConfig {

        @Bean
        public VendorAClient vendorAClient() {
            return new VendorAClient() {
                @Override
                public List<VendorAProductDTO> fetchProducts() {
                    return Arrays.asList(
                            new VendorAProductDTO("ABC123", "Product A", 8),
                            new VendorAProductDTO("LMN789", "Product C", 0)
                    );
                }
            };
        }

        @Bean
        public VendorBClient vendorBClient() {
            return new VendorBClient() {
                @Override
                public List<Product> fetchProducts() {
                    return Arrays.asList(
                            new Product(null, "ABC123", "Product A", 10, "VendorB"),
                            new Product(null, "XYZ456", "Product B", 0, "VendorB")
                    );
                }
            };
        }
    }

    @Test
    void fullIntegrationTest() {
        stockSyncService.syncStock();

        List<Product> products = productRepository.findAll();
        assertThat(products).isNotEmpty();
        assertThat(products.size()).isGreaterThanOrEqualTo(4);

        List<StockEvent> events = stockEventRepository.findAll();
        assertThat(events).isNotEmpty();

        Product[] response = restTemplate.getForObject("/products", Product[].class);
        assertThat(response).isNotNull();
        assertThat(response.length).isEqualTo(products.size());
    }
}