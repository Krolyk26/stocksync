package com.example.stocksync.entity.product.dto;

import jakarta.validation.constraints.*;

public record ProductUpdateRequest(
    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 255, message = "Name must be at most 255 chars")
    String name,

    @NotNull(message = "Stock quantity cannot be null")
    @PositiveOrZero(message = "Stock quantity must be >= 0")
    Integer stockQuantity
) {}
