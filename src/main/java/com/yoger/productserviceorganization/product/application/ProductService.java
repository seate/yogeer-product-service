package com.yoger.productserviceorganization.product.application;

import com.yoger.productserviceorganization.product.adapters.web.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.request.UpdatedDemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SellableProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleDemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleSellableProductResponseDTO;
import jakarta.validation.Valid;
import java.util.List;

public interface ProductService {
    List<SimpleSellableProductResponseDTO> findSimpleSellableProducts();

    DemoProductResponseDTO saveDemoProduct(@Valid DemoProductRequestDTO demoProductRequestDTO);

    List<SimpleDemoProductResponseDTO> findSimpleDemoProducts();

    SellableProductResponseDTO findSellableProduct(Long productId);

    DemoProductResponseDTO findDemoProduct(Long productId);

    DemoProductResponseDTO updateDemoProduct(Long productId, UpdatedDemoProductRequestDTO updatedDemoProductRequestDTO);

    void deleteProduct(Long productId, Long creatorId);
}
