package com.example.stocksync.entity.product.mapper;

import com.example.stocksync.entity.product.Product;
import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import com.example.stocksync.entity.product.dto.ProductResponse;
import com.example.stocksync.entity.product.dto.ProductUpdateRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductCreateRequest dto);

    Product toEntity(ProductResponse dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Product entity, ProductUpdateRequest dto);

    ProductResponse toResponse(Product entity);

    List<ProductResponse> toResponseList(List<Product> entities);
}