package com.example.stocksync.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"sku", "vendor"}))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String name;
    private Integer stockQuantity;
    private String vendor;

    public Product() {}

    public Product(Long id, String sku, String name, Integer stockQuantity, String vendor) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.vendor = vendor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }
}