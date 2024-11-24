package com.yoger.productserviceorganization.priceOffer.mapper;

import com.yoger.productserviceorganization.priceOffer.adapters.persistence.jpa.PriceOfferEntity;
import com.yoger.productserviceorganization.priceOffer.domain.model.PriceOffer;
import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import java.util.List;

public class PriceOfferMapper {

    public static PriceOfferEntity toEntityFrom(PriceOffer priceOffer) {
        return PriceOfferEntity.of(
                priceOffer.getProductId(),
                priceOffer.getCompanyId(),
                priceOffer.getPriceByQuantities(),
                priceOffer.getState()
        );
    }

    public static PriceOffer toDomainFrom(PriceOfferEntity priceOfferEntity) {
        return PriceOffer.of(
                priceOfferEntity.getId().getProductId(),
                priceOfferEntity.getId().getCompanyId(),
                priceOfferEntity.getPriceByQuantities(),
                priceOfferEntity.getState()
        );
    }

    public static PriceOffer createTemporary(Long productId, Long companyId, List<PriceByQuantity> priceByQuantities) {
        return PriceOffer.createTemporary(
                productId,
                companyId,
                priceByQuantities
        );
    }
}
