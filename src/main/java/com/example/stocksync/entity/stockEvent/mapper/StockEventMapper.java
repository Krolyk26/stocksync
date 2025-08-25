package com.example.stocksync.entity.stockEvent.mapper;

import com.example.stocksync.entity.stockEvent.StockEvent;
import com.example.stocksync.entity.stockEvent.dto.StockEventResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockEventMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "sku",       source = "product.sku")
    @Mapping(target = "vendor",    source = "product.vendor")
    StockEventResponse toResponse(StockEvent entity);

    List<StockEventResponse> toResponseList(List<StockEvent> entities);

}
