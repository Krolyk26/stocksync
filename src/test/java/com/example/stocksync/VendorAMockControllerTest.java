package com.example.stocksync;

import com.example.stocksync.controller.VendorAMockController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VendorAMockController.class)
class VendorAMockControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void getVendorAProducts_returnsMockedList() throws Exception {
        mockMvc.perform(get("/mock/vendor-a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("ABC123"))
                .andExpect(jsonPath("$[1].stockQuantity").value(2));
    }
}
