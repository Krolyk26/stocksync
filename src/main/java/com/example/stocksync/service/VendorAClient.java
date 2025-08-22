package com.example.stocksync.service;
import com.example.stocksync.dto.VendorAProductDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class VendorAClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String VENDOR_A_URL = "http://localhost:8080/mock/vendor-a";

    public List<VendorAProductDTO> fetchProducts() {
        VendorAProductDTO[] response = restTemplate.getForObject(VENDOR_A_URL, VendorAProductDTO[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }
}
