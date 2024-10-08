package com.yoger.productserviceorganization.product.dto.response;

import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.persistence.ProductEntity;
import java.util.List;

public record SimpleDemoProductResponseDTO(
        Long id,
        String name,
        List<PriceByQuantity> priceByQuantities,
        String thumbnailImageUrl,
        String creatorName,
        ProductState state
) {
    public static SimpleDemoProductResponseDTO from(ProductEntity productEntity) {
        return new SimpleDemoProductResponseDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPriceByQuantities(),
                productEntity.getThumbnailImageUrl(),
                productEntity.getCreatorName(),
                productEntity.getState()
        );
    }
}
