package com.yoger.productserviceorganization.priceOffer.domain.port;

import com.yoger.productserviceorganization.priceOffer.domain.model.PriceOffer;
import java.util.List;
import java.util.Optional;

public interface PriceOfferRepository {
    void save(PriceOffer priceOffer);

    Optional<PriceOffer> findById(Long productId, Long companyId);

    List<PriceOffer> findAllByProductId(Long productId);
}