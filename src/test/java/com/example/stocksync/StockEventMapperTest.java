package com.example.stocksync;

import com.example.stocksync.entity.product.Product;
import com.example.stocksync.entity.stockEvent.StockEvent;
import com.example.stocksync.entity.stockEvent.StockEventStatus;
import com.example.stocksync.entity.stockEvent.dto.StockEventResponse;
import com.example.stocksync.entity.stockEvent.mapper.StockEventMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class StockEventMapperTest {

    private final StockEventMapper mapper = Mappers.getMapper(StockEventMapper.class);

    @Test
    void toResponse_mapsNestedProductFields() {
        Product product = new Product("SKU-E1", "Event P", 1, "Vendor E");
        StockEvent event = new StockEvent(product, StockEventStatus.IN_STOCK);

        StockEventResponse stockEventResponse = mapper.toResponse(event);

        assertNull(stockEventResponse.id());
        assertEquals("SKU-E1", stockEventResponse.sku());
        assertEquals("Vendor E", stockEventResponse.vendor());
        assertEquals(StockEventStatus.IN_STOCK, stockEventResponse.status());
    }
}


