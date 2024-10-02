package com.yoger.productserviceorganization.proruct.controller;

import com.yoger.productserviceorganization.proruct.domain.ProductService;
import com.yoger.productserviceorganization.proruct.dto.response.SellableProductResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<SellableProductResponseDTO> getSellableProducts() {
        return productService.viewSellableProducts();
    }
}
