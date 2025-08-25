package com.example.stocksync;

import com.example.stocksync.entity.product.Product;
import com.example.stocksync.entity.stockEvent.StockEvent;
import com.example.stocksync.entity.stockEvent.StockEventStatus;
import com.example.stocksync.entity.stockEvent.dto.StockEventCreateRequest;
import com.example.stocksync.entity.stockEvent.dto.StockEventResponse;
import com.example.stocksync.entity.stockEvent.mapper.StockEventMapper;
import com.example.stocksync.repository.ProductRepository;
import com.example.stocksync.repository.StockEventRepository;
import com.example.stocksync.service.stockEventService.StockEventService;
import com.example.stocksync.service.stockEventService.StockEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockEventServiceImplTest {

    @Mock private StockEventRepository stockEventRepository;
    @Mock private ProductRepository productRepository;

    private StockEventService stockEventService;
    private StockEventMapper stockEventMapper;

    @BeforeEach
    void setUp() {
        stockEventMapper = Mappers.getMapper(StockEventMapper.class);
        stockEventService = new StockEventServiceImpl(stockEventRepository, productRepository, stockEventMapper);
    }

    @Test
    void create_and_get_stock_event() {
        Product product = new Product("SKU-Z", "Zeta", 1, "Vendor Z");
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        when(stockEventRepository.save(any(StockEvent.class))).thenAnswer(inv -> {
            StockEvent stockEvent = inv.getArgument(0);
            stockEvent.setId(100L);
            return stockEvent;
        });
        when(stockEventRepository.findById(100L)).thenAnswer(inv -> {
            StockEvent stockEvent = new StockEvent(product, StockEventStatus.OUT_OF_STOCK);
            stockEvent.setId(100L);
            return Optional.of(stockEvent);
        });

        StockEventResponse created = stockEventService.createStockEvent(new StockEventCreateRequest(1L, StockEventStatus.OUT_OF_STOCK));

        assertNotNull(created.id());
        assertEquals(1L, created.productId());
        assertEquals(StockEventStatus.OUT_OF_STOCK, created.status());

        StockEventResponse fetched = stockEventService.getStockEvent(created.id());
        assertEquals(created.id(), fetched.id());
    }

    @Test
    void getAll_returnsAllEvents() {
        Product product = new Product("SKU-Z2", "Zeta2", 3, "Vendor Z");
        product.setId(2L);
        when(stockEventRepository.findAll()).thenReturn(List.of(new StockEvent(product, StockEventStatus.OUT_OF_STOCK), new StockEvent(product, StockEventStatus.IN_STOCK)));

        assertEquals(2, stockEventService.getAllStockEvents().size());
    }

    @Test
    void createStockEvent_throws_whenProductNotFound() {
        when(productRepository.findById(99999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                stockEventService.createStockEvent(new StockEventCreateRequest(99999L, StockEventStatus.IN_STOCK))
        );
    }
}
