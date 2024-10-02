package com.yoger.productserviceorganization.proruct.domain;

import com.yoger.productserviceorganization.proruct.dto.response.SellableProductResponseDTO;
import com.yoger.productserviceorganization.proruct.persistence.ProductEntity;
import com.yoger.productserviceorganization.proruct.persistence.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<SellableProductResponseDTO> viewSellableProducts() {
        return productRepository.findAll().stream()
                .filter(productEntity -> productEntity.getState() == ProductState.SELLABLE)
                .map(this::mapToSellableDTO)
                .toList();
    }

    private SellableProductResponseDTO mapToSellableDTO(ProductEntity productEntity) {
        return SellableProductResponseDTO.from(productEntity);
    }
}
