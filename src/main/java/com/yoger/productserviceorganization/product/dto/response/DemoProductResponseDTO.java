package com.yoger.productserviceorganization.product.dto.response;

import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.persistence.ProductEntity;

public record DemoProductResponseDTO(
        Long id,
        String name,
        String description,
        String imageUrl,
        Long creatorId,
        String creatorName,
        ProductState state
        ) {
    public static DemoProductResponseDTO from(ProductEntity productEntity) {
        return new DemoProductResponseDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getImageUrl(),
                productEntity.getCreatorId(),
                productEntity.getCreatorName(),
                productEntity.getState()
        );
    }
}
