package com.example.stocksync.service;
import com.example.stocksync.entity.Product;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorBClient {

    private static final String CSV_FILE_PATH = "vendor-b/stock.csv"; // у ресурсах
    private static final String VENDOR_NAME = "VendorB";

    public List<Product> fetchProducts() {
        List<Product> products = new ArrayList<>();

        try {
            ClassPathResource resource = new ClassPathResource(CSV_FILE_PATH);
            try (Reader in = new InputStreamReader(resource.getInputStream())) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT
                        .withHeader("sku", "name", "stockQuantity")
                        .withFirstRecordAsHeader()
                        .parse(in);

                for (CSVRecord record : records) {
                    String sku = record.get("sku");
                    String name = record.get("name");
                    Integer stockQuantity = Integer.parseInt(record.get("stockQuantity"));

                    Product p = new Product();
                    p.setSku(sku);
                    p.setName(name);
                    p.setStockQuantity(stockQuantity);
                    p.setVendor(VENDOR_NAME);

                    products.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}