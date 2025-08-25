package com.example.stocksync;

import com.example.stocksync.entity.product.Product;
import com.example.stocksync.entity.product.dto.ProductCreateRequest;
import com.example.stocksync.entity.product.dto.ProductResponse;
import com.example.stocksync.entity.product.dto.ProductUpdateRequest;
import com.example.stocksync.entity.product.mapper.ProductMapper;
import com.example.stocksync.repository.ProductRepository;
import com.example.stocksync.service.productService.ProductService;
import com.example.stocksync.service.productService.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;

    private ProductService productService;
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
        productService = new ProductServiceImpl(productRepository, productMapper);
    }

    @Test
    void create_and_get_and_update_product() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("SKU-X", "Name X", 7, "Vendor X");

        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product product = inv.getArgument(0);
            product.setId(1L);
            return product;
        });

        Product createdEntity = new Product("SKU-X", "Name X", 7, "Vendor X");
        createdEntity.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(createdEntity));

        ProductResponse created = productService.createProduct(productCreateRequest);
        assertNotNull(created.id());
        assertEquals("SKU-X", created.sku());

        ProductResponse fetched = productService.getProductById(created.id());
        assertEquals(created.id(), fetched.id());

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest("Name X2", 0);
        ProductResponse updatedProduct = productService.updateProduct(created.id(), productUpdateRequest);
        assertEquals("Name X2", updatedProduct.name());
        assertEquals(0, updatedProduct.stockQuantity());

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, atLeastOnce()).save(captor.capture());
        assertEquals("Name X2", captor.getValue().getName());
        assertEquals(0, captor.getValue().getStockQuantity());
    }

    @Test
    void updateProduct_throws_whenNotFound() {
        when(productRepository.findById(99999L)).thenReturn(Optional.empty());
        ProductUpdateRequest updatedProduct = new ProductUpdateRequest("Any", 1);
        assertThrows(RuntimeException.class, () -> productService.updateProduct(99999L, updatedProduct));
    }

    @Test
    void deleteProduct_callsRepositoryDelete() {
        Product existingProduct = new Product("SKU-DEL", "To Delete", 2, "Vendor D");
        existingProduct.setId(10L);
        when(productRepository.findById(10L)).thenReturn(Optional.of(existingProduct));

        productService.deleteProduct(10L);

        verify(productRepository, times(1)).delete(any(Product.class));
    }
}
