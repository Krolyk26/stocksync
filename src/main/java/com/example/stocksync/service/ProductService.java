package com.example.stocksync.service;

import com.example.stocksync.dto.ProductDTO;
import com.example.stocksync.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductDTO(
                        product.getSku(),
                        product.getName(),
                        product.getStockQuantity(),
                        product.getVendor()))
                .collect(Collectors.toList());
    }
}
