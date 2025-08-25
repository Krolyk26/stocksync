package com.example.stocksync.entity.product.dto;

import java.time.LocalDateTime;

public record ProductResponse(
    Long id,
    String sku,
    String name,
    Integer stockQuantity,
    String vendor,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
