package com.yoger.productserviceorganization.priceOffer.adapters.web.dto.request;

import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PriceOfferRequestDTO(@NotEmpty List<PriceByQuantity> priceByQuantities) {
}
