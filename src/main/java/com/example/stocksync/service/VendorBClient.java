package com.example.stocksync.service;

import com.example.stocksync.dto.VendorBProductDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorBClient {

    private static final Logger log = LoggerFactory.getLogger(VendorBClient.class);
    private static final String VENDOR_NAME = "VendorB";
    private final String csvFilePath;

    public VendorBClient(@Value("${vendor.b.csv}") String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public List<VendorBProductDTO> fetchProducts() {
        List<VendorBProductDTO> products = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (Reader in = new InputStreamReader(resource.getInputStream())) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT
                        .withHeader("sku", "name", "stockQuantity")
                        .withFirstRecordAsHeader()
                        .parse(in);

                for (CSVRecord record : records) {
                    try {
                        String sku = record.get("sku");
                        String name = record.get("name");
                        Integer stockQuantity = Integer.parseInt(record.get("stockQuantity"));
                        VendorBProductDTO productDTO = new VendorBProductDTO(sku, name, stockQuantity, VENDOR_NAME);
                        products.add(productDTO);
                    } catch (NumberFormatException e) {
                        log.error("Error parsing stock quantity for SKU '{}' in CSV file: {}", record.get("sku"), e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error reading CSV file from path: {}", csvFilePath, e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing CSV: {}", e.getMessage(), e);
        }
        return products;
    }
}