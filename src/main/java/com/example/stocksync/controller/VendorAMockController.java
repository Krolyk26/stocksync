package com.example.stocksync.controller;
import com.example.stocksync.dto.VendorAProductDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class VendorAMockController {

    @GetMapping("/mock/vendor-a")
    public List<VendorAProductDTO> getVendorAProducts() {
        return Arrays.asList(
                new VendorAProductDTO("ABC123", "Product A", 8),
                new VendorAProductDTO("LMN789", "Product C", 0)
        );
    }
}
