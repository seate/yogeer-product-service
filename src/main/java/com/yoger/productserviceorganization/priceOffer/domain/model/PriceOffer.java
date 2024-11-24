package com.yoger.productserviceorganization.priceOffer.domain.model;

import com.yoger.productserviceorganization.priceOffer.domain.exception.PriceOfferNotUpdatableException;
import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import java.util.List;
import lombok.Getter;

@Getter
public final class PriceOffer {
    private final Long productId;
    private final Long companyId;
    private final List<PriceByQuantity> priceByQuantities;
    private PriceOfferState state;

    private PriceOffer(Long productId, Long companyId, List<PriceByQuantity> priceByQuantities, PriceOfferState state) {
        this.productId = productId;
        this.companyId = companyId;
        this.priceByQuantities = priceByQuantities;
        this.state = state;
    }

    public static PriceOffer of(Long productId, Long companyId, List<PriceByQuantity> priceByQuantities,
                                PriceOfferState state) {
        return new PriceOffer(
                productId,
                companyId,
                priceByQuantities,
                state
        );
    }

    public static PriceOffer createTemporary(Long productId, Long companyId, List<PriceByQuantity> priceByQuantities) {
        return new PriceOffer(
                productId,
                companyId,
                priceByQuantities,
                PriceOfferState.TEMPORARY
        );
    }

    public void confirm() {
        if (state != PriceOfferState.TEMPORARY) {
            throw new PriceOfferNotUpdatableException("제안을 확정할 수 없는 상태입니다.");
        }

        this.state = PriceOfferState.CONFIRMED;
    }

}
