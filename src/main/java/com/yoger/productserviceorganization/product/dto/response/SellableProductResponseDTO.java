package com.yoger.productserviceorganization.product.dto.response;

import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.persistence.ProductEntity;
import java.time.LocalDateTime;
import java.util.List;

public record SellableProductResponseDTO(
        Long id,
        String name,
        List<PriceByQuantity> priceByQuantities,
        String description,
        String imageUrl,
        ProductState state,
        Long creatorId,
        String creatorName,
        LocalDateTime dueDate,
        int soldAmount
) {
    public static SellableProductResponseDTO from(ProductEntity productEntity) {
        return new SellableProductResponseDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPriceByQuantities(),
                productEntity.getDescription(),
                productEntity.getImageUrl(),
                productEntity.getState(),
                productEntity.getCreatorId(),
                productEntity.getCreatorName(),
                productEntity.getDueDate(),
                productEntity.getInitialStockQuantity()- productEntity.getStockQuantity()
        );
    }
}
