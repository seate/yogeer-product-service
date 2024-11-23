package com.yoger.productserviceorganization.priceOffer.adapters.persistence.jpa;

import com.yoger.productserviceorganization.priceOffer.domain.model.PriceOfferState;
import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PriceOfferEntity {

    @EmbeddedId
    private PriceOfferId id;

    @Nullable
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<PriceByQuantity> priceByQuantities;

    private PriceOfferState state;


    public static PriceOfferEntity of(Long productId, Long companyId, List<PriceByQuantity> priceOffers,
                                      PriceOfferState priceOfferState) {
        return new PriceOfferEntity(PriceOfferId.of(productId, companyId), priceOffers, priceOfferState);
    }
}
