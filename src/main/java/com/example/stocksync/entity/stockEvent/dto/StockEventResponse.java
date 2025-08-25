package com.example.stocksync.entity.stockEvent.dto;

import com.example.stocksync.entity.stockEvent.StockEventStatus;

import java.time.LocalDateTime;

public record StockEventResponse(
    Long id,
    Long productId,
    String sku,
    String vendor,
    StockEventStatus status,
    LocalDateTime eventTime
) {}
