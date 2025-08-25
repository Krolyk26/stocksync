package com.example.stocksync.entity.stockEvent.dto;

import com.example.stocksync.entity.stockEvent.StockEventStatus;
import jakarta.validation.constraints.NotNull;

public record StockEventCreateRequest(
    @NotNull(message = "Product id cannot be null")
    Long productId,

    @NotNull(message = "Stock event status cannot be null")
    StockEventStatus status
) {}
