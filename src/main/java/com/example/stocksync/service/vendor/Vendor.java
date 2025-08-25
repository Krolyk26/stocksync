package com.example.stocksync.service.vendor;

import com.example.stocksync.entity.product.dto.ProductCreateRequest;

import java.util.List;

public interface Vendor {
    List<ProductCreateRequest> fetchProducts();
}
