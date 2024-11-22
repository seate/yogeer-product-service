package com.yoger.productserviceorganization.product.adapters.web.dto.request;

public record StockChangeRequestDTO(
        Integer quantity
) {
    public static StockChangeRequestDTO of(Integer quantity) {
        return new StockChangeRequestDTO(quantity);
    }
}
