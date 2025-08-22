package com.example.stocksync.dto;

public record VendorAProductDTO(
        String sku,
        String name,
        Integer stockQuantity
) {}
