package com.example.stocksync;

import com.example.stocksync.entity.product.Product;
import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import com.example.stocksync.entity.product.dto.ProductResponse;
import com.example.stocksync.entity.product.dto.ProductUpdateRequest;
import com.example.stocksync.entity.product.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void toEntity_fromCreateRequest_mapsFields() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("SKU-M1", "Map Name", 9, "Vendor M");
        Product product = mapper.toEntity(productCreateRequest);

        assertNull(product.getId());
        assertEquals("SKU-M1", product.getSku());
        assertEquals("Map Name", product.getName());
        assertEquals(9, product.getStockQuantity());
        assertEquals("Vendor M", product.getVendor());
    }

    @Test
    void updateEntity_appliesNonNullFields_only() {
        Product product = new Product("SKU-U1", "Old", 3, "Vendor U");
        ProductUpdateRequest updateRequest = new ProductUpdateRequest("New Name", 0);

        mapper.updateEntity(product, updateRequest);

        assertEquals("New Name", product.getName());
        assertEquals(0, product.getStockQuantity());
        assertEquals("SKU-U1", product.getSku());
        assertEquals("Vendor U", product.getVendor());
    }

    @Test
    void toResponse_roundTrips_basicFields() {
        Product product = new Product("SKU-R1", "Resp", 7, "Vendor R");
        ProductResponse productResponse = mapper.toResponse(product);

        assertEquals("SKU-R1", productResponse.sku());
        assertEquals("Resp", productResponse.name());
        assertEquals(7, productResponse.stockQuantity());
        assertEquals("Vendor R", productResponse.vendor());
    }
}


