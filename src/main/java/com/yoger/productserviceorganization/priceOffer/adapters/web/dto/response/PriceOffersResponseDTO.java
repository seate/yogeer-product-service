package com.yoger.productserviceorganization.priceOffer.adapters.web.dto.response;

import com.yoger.productserviceorganization.priceOffer.domain.model.PriceOffer;
import java.util.List;

public record PriceOffersResponseDTO(List<PriceOffer> priceOffers) {

    public static PriceOffersResponseDTO from(List<PriceOffer> priceOffer) {
        return new PriceOffersResponseDTO(priceOffer);
    }
}
