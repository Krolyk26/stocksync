package com.example.stocksync.service.vendor;
import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VendorAClient implements Vendor {
    private static final Logger log = LoggerFactory.getLogger(VendorAClient.class);

    private final RestTemplate restTemplate;

    @Value("${vendor.a.url}")
    private String vendorAUrl;

    @Override
    @Retryable(
            retryFor = { ResourceAccessException.class, HttpServerErrorException.class },
            noRetryFor = { HttpClientErrorException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 2)
    )
    public List<ProductCreateRequest> fetchProducts() {
        log.info("Fetching products from VendorA: {}", vendorAUrl);
        ProductCreateRequest[] payload = restTemplate.getForObject(vendorAUrl, ProductCreateRequest[].class);

        if (payload == null) {
            log.warn("VendorA returned null payload. url={}", vendorAUrl);
            return List.of();
        }

        List<ProductCreateRequest> list = Arrays.stream(payload)
                .filter(Objects::nonNull)
                .toList();
        log.info("VendorA fetched {} items", list.size());
        return list;
    }

    @Recover
    public List<ProductCreateRequest> recover(RestClientException ex) {
        log.error("VendorA exhausted retries: {}", ex.getMessage(), ex);
        return List.of();
    }
}