package com.example.stocksync.entity.product.dto;

import jakarta.validation.constraints.*;

public record ProductCreateRequest(
        @NotBlank(message = "SKU cannot be blank")
        @Size(max = 40, message = "SKU must be at most 40 chars")
        @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "SKU may contain letters, digits, dot, dash, underscore")
        String sku,

        @NotBlank(message = "Product name cannot be blank")
        @Size(max = 255, message = "Name must be at most 255 chars")
        String name,

        @NotNull(message = "Stock quantity cannot be null")
        @PositiveOrZero(message = "Stock quantity must be >= 0")
        Integer stockQuantity,

        @NotBlank(message = "Vendor cannot be blank")
        String vendor
) {}