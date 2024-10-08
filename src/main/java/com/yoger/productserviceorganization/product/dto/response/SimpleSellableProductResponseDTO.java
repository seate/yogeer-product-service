package com.yoger.productserviceorganization.product.dto.response;

import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.persistence.ProductEntity;
import java.time.LocalDateTime;
import java.util.List;

public record SimpleSellableProductResponseDTO(
        Long id,
        String name,
        List<PriceByQuantity> priceByQuantities,
        String thumbnailImageUrl,
        String creatorName,
        int soldQuantity,
        LocalDateTime dueDate,
        ProductState state
) {
    public static SimpleSellableProductResponseDTO from(ProductEntity productEntity) {
        int soldQuantity = productEntity.getInitialStockQuantity() - productEntity.getStockQuantity(); // 판매된 수량 계산
        return new SimpleSellableProductResponseDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPriceByQuantities(),
                productEntity.getThumbnailImageUrl(),
                productEntity.getCreatorName(),
                soldQuantity,
                productEntity.getDueDate(),
                productEntity.getState()
        );
    }
}
