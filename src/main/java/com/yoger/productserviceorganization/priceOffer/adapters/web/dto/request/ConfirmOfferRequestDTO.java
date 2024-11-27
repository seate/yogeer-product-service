package com.yoger.productserviceorganization.priceOffer.adapters.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record ConfirmOfferRequestDTO(@NotNull Long companyId) {
}
