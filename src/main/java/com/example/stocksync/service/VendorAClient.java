package com.example.stocksync.service;
import com.example.stocksync.dto.VendorAProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class VendorAClient {

    private final RestTemplate restTemplate;
    private final String vendorAUrl;

    public VendorAClient(RestTemplate restTemplate,
                         @Value("${vendor.a.url}") String vendorAUrl) {
        this.restTemplate = restTemplate;
        this.vendorAUrl = vendorAUrl;
    }

    public List<VendorAProductDTO> fetchProducts() {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(vendorAUrl, VendorAProductDTO[].class)));
    }
}