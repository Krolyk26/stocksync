package com.example.stocksync.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"sku", "vendor"}))
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String name;
    private Integer stockQuantity;
    private String vendor;

    public Product(String sku, String name, Integer stockQuantity, String vendor) {
        this.sku = sku;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.vendor = vendor;
    }
}