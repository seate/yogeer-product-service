package com.yoger.productserviceorganization.proruct.dto.response;

import com.yoger.productserviceorganization.proruct.domain.ProductState;

public record SellableProductResponseDTO(
        String name,
        String priceByQuantity,
        String description,
        String imageUrl,
        ProductState state
) {
    public static SellableProductResponseDTO of(String name, String priceByQuantity, String description, String imageUrl,
                                                ProductState state) {
        return new SellableProductResponseDTO(name, priceByQuantity, description, imageUrl, state);
    }
}
