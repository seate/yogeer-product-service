package com.yoger.productserviceorganization.product.dto.response;

import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import java.time.LocalDateTime;
import java.util.List;

public record SimpleSellableProductResponseDTO(
        Long id,
        String name,
        List<PriceByQuantity> priceByQuantities,
        String thumbnailImageUrl,
        String creatorName,
        int soldAmount,
        LocalDateTime dueDate,
        ProductState state
) {
}
