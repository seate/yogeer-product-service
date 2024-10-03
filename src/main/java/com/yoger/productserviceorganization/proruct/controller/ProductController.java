package com.yoger.productserviceorganization.proruct.controller;

import com.yoger.productserviceorganization.proruct.domain.ProductService;
import com.yoger.productserviceorganization.proruct.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.proruct.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.proruct.dto.response.SellableProductResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DemoProductResponseDTO saveDemo(@Valid @ModelAttribute DemoProductRequestDTO demoProductRequestDTO) {
        return productService.saveDemoProduct(demoProductRequestDTO);
    }
}
