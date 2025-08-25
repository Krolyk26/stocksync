package com.example.stocksync.service.productService;

import com.example.stocksync.entity.product.Product;
import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import com.example.stocksync.entity.product.dto.ProductResponse;
import com.example.stocksync.entity.product.dto.ProductUpdateRequest;
import com.example.stocksync.entity.product.mapper.ProductMapper;
import com.example.stocksync.exception.NotFoundEntityException;
import com.example.stocksync.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> responses = productRepository.findAll().stream().map(productMapper::toResponse).toList();
        log.info("Fetched {} products", responses.size());
        return responses;
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest product) {
        ProductResponse response = productMapper.toResponse(productRepository.save(productMapper.toEntity(product)));
        log.info("Created product id={} sku={} vendor={}", response.id(), response.sku(), response.vendor());
        return response;
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundEntityException("Product", productId));
        productMapper.updateEntity((product), productUpdateRequest);
        ProductResponse response = productMapper.toResponse(productRepository.save(product));
        log.info("Updated product id={} newStock={}", response.id(), response.stockQuantity());
        return response;
    }

    @Override
    public void deleteProduct(Long productId) {
        ProductResponse toDelete = getProductById(productId);
        productRepository.delete(productMapper.toEntity(toDelete));
        log.info("Deleted product id={} sku={} vendor={}", toDelete.id(), toDelete.sku(), toDelete.vendor());
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        ProductResponse response = productMapper.toResponse(productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundEntityException("Product", productId)));
        log.info("Fetched product id={} sku={} vendor={}", response.id(), response.sku(), response.vendor());
        return response;
    }

    @Override
    public Optional<ProductResponse> findBySkuAndVendor(String sku, String vendor) {
        Optional<ProductResponse> result = productRepository.findBySkuAndVendor(sku, vendor).map(productMapper::toResponse);
        log.debug("findBySkuAndVendor sku={} vendor={} found={}", sku, vendor, result.isPresent());
        return result;
    }

}
