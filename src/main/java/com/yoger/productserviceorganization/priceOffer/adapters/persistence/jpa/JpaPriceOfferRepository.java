package com.yoger.productserviceorganization.priceOffer.adapters.persistence.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPriceOfferRepository extends JpaRepository<PriceOfferEntity, PriceOfferId> {

    List<PriceOfferEntity> findAllById_ProductId(Long productId);
}
