package com.yoger.productserviceorganization.proruct.dto.response;

import com.yoger.productserviceorganization.proruct.domain.model.ProductState;
import com.yoger.productserviceorganization.proruct.persistence.ProductEntity;

public record SellableProductResponseDTO(
        Long id,
        String name,
        String priceByQuantity,
        String description,
        String imageUrl,
        ProductState state
) {
    public static SellableProductResponseDTO from(ProductEntity productEntity) {
        return new SellableProductResponseDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPriceByQuantity(),
                productEntity.getDescription(),
                productEntity.getImageUrl(),
                productEntity.getState()
        );
    }
}
