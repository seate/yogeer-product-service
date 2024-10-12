package com.yoger.productserviceorganization.product.dto.response;

import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import com.yoger.productserviceorganization.product.domain.model.Product;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
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
        int soldQuantity
) {
    public static SellableProductResponseDTO from(Product product) {
        int soldQuantity = product.getInitialStockQuantity() - product.getStockQuantity(); // 판매된 수량 계산
        return new SellableProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPriceByQuantities(),
                product.getDescription(),
                product.getImageUrl(),
                product.getState(),
                product.getCreatorId(),
                product.getCreatorName(),
                product.getDueDate(),
                soldQuantity
        );
    }
}
