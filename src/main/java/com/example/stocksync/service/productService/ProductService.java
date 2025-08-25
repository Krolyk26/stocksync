package com.example.stocksync.service.productService;

import com.example.stocksync.entity.product.Product;
import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import com.example.stocksync.entity.product.dto.ProductResponse;
import com.example.stocksync.entity.product.dto.ProductUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface ProductService {

    ProductResponse createProduct(@Valid ProductCreateRequest product);
    ProductResponse updateProduct(Long id, @Valid ProductUpdateRequest product);
    void deleteProduct(Long productId);
    ProductResponse getProductById(Long productId);
    Optional<ProductResponse> findBySkuAndVendor(String sku, String vendor);
    List<ProductResponse> getAllProducts();

}
