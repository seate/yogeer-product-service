package com.yoger.productserviceorganization.priceOffer.adapters.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PriceOfferId implements Serializable {

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long companyId;

    public static PriceOfferId of(Long productId, Long companyId) {
        return new PriceOfferId(productId, companyId);
    }
}
