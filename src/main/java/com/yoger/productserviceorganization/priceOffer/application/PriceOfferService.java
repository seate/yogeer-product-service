package com.yoger.productserviceorganization.priceOffer.application;

import com.yoger.productserviceorganization.priceOffer.adapters.web.dto.request.PriceOfferRequestDTO;

public interface PriceOfferService {

    void create(Long productId, Long companyId, PriceOfferRequestDTO priceOfferRequestDTO);
}
