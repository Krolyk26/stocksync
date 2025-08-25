package com.example.stocksync.service.vendor;

import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorBClient implements Vendor {

    private static final Logger log = LoggerFactory.getLogger(VendorBClient.class);
    private static final String VENDOR_NAME = "Vendor B";
    private final ResourceLoader resourceLoader;
    @Value("${vendor.b.csv}")
    private String csvLocation;

    @Override
    public List<ProductCreateRequest> fetchProducts() {
        log.info("Fetching products from VendorB CSV: {}", csvLocation);
        Resource resource = resourceLoader.getResource(csvLocation);
        if (!resource.exists()) {
            log.warn("VendorB CSV not found: {}", csvLocation);
            return List.of();
        }

        List<ProductCreateRequest> products = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader("sku", "name", "stockQuantity")
                    .withFirstRecordAsHeader()
                    .parse(reader);

            for (CSVRecord r : records) {
                String sku = trim(r.get("sku"));
                String name = trim(r.get("name"));
                if (isBlank(sku) || isBlank(name)) continue;

                int qty = parseQty(r.get("stockQuantity"));
                products.add(new ProductCreateRequest(sku, name, Math.max(qty, 0), VENDOR_NAME));
            }
        } catch (Exception e) {
            log.error("VendorB CSV read error from {}: {}", csvLocation, e.getMessage());
            return List.of();
        }
        log.info("VendorB fetched {} items", products.size());
        return products;
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
    private static int parseQty(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
}