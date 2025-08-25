package com.example.stocksync.service.stockEventService;

import com.example.stocksync.entity.stockEvent.dto.StockEventCreateRequest;
import com.example.stocksync.entity.stockEvent.dto.StockEventResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface StockEventService {
    StockEventResponse createStockEvent(@Valid  StockEventCreateRequest stockEvent);
    StockEventResponse getStockEvent(Long stockEventId);
    List<StockEventResponse> getAllStockEvents();
}
