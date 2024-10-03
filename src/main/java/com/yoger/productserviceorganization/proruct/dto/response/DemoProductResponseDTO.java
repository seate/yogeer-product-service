package com.yoger.productserviceorganization.proruct.dto.response;

import com.yoger.productserviceorganization.proruct.domain.ProductState;
import com.yoger.productserviceorganization.proruct.persistence.ProductEntity;

public record DemoProductResponseDTO(
        Long id,
        String name,
        String description,
        String imageUrl,
        ProductState state
) {
    public static DemoProductResponseDTO from(ProductEntity productEntity) {
        return new DemoProductResponseDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getImageUrl(),
                productEntity.getState()
        );
    }
}
