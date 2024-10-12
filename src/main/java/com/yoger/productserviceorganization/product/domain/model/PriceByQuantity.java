package com.yoger.productserviceorganization.product.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PriceByQuantity(
        @NotNull(message = "수량에 대한 정보를 입력해주세요.")
        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        int quantity,

        @NotNull(message = "가격에 대한 정보를 입력해주세요.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        int price
) {
}
