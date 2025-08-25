package com.example.stocksync;

import com.example.stocksync.controller.ProductController;
import com.example.stocksync.entity.product.dto.ProductResponse;
import com.example.stocksync.service.productService.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean
    private ProductServiceImpl productService;

    @Test
    void getAllProducts_returnsList() throws Exception {
        List<ProductResponse> data = List.of(
                new ProductResponse(1L, "SKU1", "Name1", 5, "Vendor A", LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponse(2L, "SKU2", "Name2", 0, "Vendor B", LocalDateTime.now(), LocalDateTime.now())
        );
        when(productService.getAllProducts()).thenReturn(data);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU1"))
                .andExpect(jsonPath("$[1].stockQuantity").value(0));
    }

    @Test
    void getAllProducts_returnsEmptyList() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
