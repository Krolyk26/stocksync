package com.example.stocksync.service.stockEventService;

import com.example.stocksync.entity.product.Product;
import com.example.stocksync.entity.stockEvent.StockEvent;
import com.example.stocksync.entity.stockEvent.dto.StockEventCreateRequest;
import com.example.stocksync.entity.stockEvent.dto.StockEventResponse;
import com.example.stocksync.entity.stockEvent.mapper.StockEventMapper;
import com.example.stocksync.exception.NotFoundEntityException;
import com.example.stocksync.repository.ProductRepository;
import com.example.stocksync.repository.StockEventRepository;
import com.example.stocksync.service.productService.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockEventServiceImpl implements StockEventService {
    private static final Logger log = LoggerFactory.getLogger(StockEventServiceImpl.class);
    private final StockEventRepository stockEventRepository;
    private final ProductRepository productRepository;
    private final StockEventMapper stockEventMapper;

    @Override
    public StockEventResponse createStockEvent(StockEventCreateRequest stockEventRequest) {
        Product product = productRepository.findById(stockEventRequest.productId())
                .orElseThrow(() -> new NotFoundEntityException("Product", stockEventRequest.productId()));

        StockEvent stockEvent = new StockEvent(product, stockEventRequest.status());
        StockEventResponse response = stockEventMapper.toResponse(stockEventRepository.save(stockEvent));
        log.info("Created stock event id={} productId={} status={}", response.id(), response.productId(), response.status());
        return response;
    }

    @Override
    public StockEventResponse getStockEvent(Long stockEventId) {
        return stockEventMapper.toResponse(stockEventRepository.findById(stockEventId)
                .orElseThrow(() -> new NotFoundEntityException("StockEvent", stockEventId)));
    }

    @Override
    public List<StockEventResponse> getAllStockEvents() {
        return stockEventMapper.toResponseList(stockEventRepository.findAll());
    }
}
