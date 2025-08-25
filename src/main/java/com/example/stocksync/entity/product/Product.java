package com.example.stocksync.entity.product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sku", "vendor"}))
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String sku;

    @NotNull
    private String name;

    @NotNull
    private Integer stockQuantity;

    @NotNull
    private String vendor;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Product(String sku, String name, Integer stockQuantity, String vendor) {
        this.sku = sku;
        this.vendor = vendor;
        this.name = name;
        this.stockQuantity = (stockQuantity == null ? 0 : stockQuantity);
    }
}