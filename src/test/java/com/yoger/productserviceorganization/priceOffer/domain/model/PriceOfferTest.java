package com.yoger.productserviceorganization.priceOffer.domain.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.yoger.productserviceorganization.priceOffer.domain.exception.PriceOfferNotUpdatableException;
import org.junit.jupiter.api.Test;

class PriceOfferTest {

    @Test
    void confirm() {
        PriceOffer priceOffer = PriceOffer.of(1L, 1L, null, PriceOfferState.CONFIRMED);
        assertThatThrownBy(priceOffer::confirm)
                .isInstanceOf(PriceOfferNotUpdatableException.class);
    }
}