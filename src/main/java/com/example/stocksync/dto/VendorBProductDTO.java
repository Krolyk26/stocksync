package com.example.stocksync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record VendorBProductDTO(
        @NotBlank(message = "SKU cannot be blank")
        String sku,
        @NotBlank(message = "Product name cannot be blank")
        String name,
        @NotNull(message = "Stock quantity cannot be null")
        @PositiveOrZero(message = "Stock quantity must be a positive number or zero")
        Integer stockQuantity,
        @NotBlank(message = "Vendor name cannot be blank")
        String vendor
) {}
