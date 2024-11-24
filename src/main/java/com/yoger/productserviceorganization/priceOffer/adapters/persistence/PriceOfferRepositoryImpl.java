package com.yoger.productserviceorganization.priceOffer.adapters.persistence;

import com.yoger.productserviceorganization.priceOffer.adapters.persistence.jpa.JpaPriceOfferRepository;
import com.yoger.productserviceorganization.priceOffer.adapters.persistence.jpa.PriceOfferId;
import com.yoger.productserviceorganization.priceOffer.domain.model.PriceOffer;
import com.yoger.productserviceorganization.priceOffer.domain.port.PriceOfferRepository;
import com.yoger.productserviceorganization.priceOffer.mapper.PriceOfferMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class PriceOfferRepositoryImpl implements PriceOfferRepository {
    private final JpaPriceOfferRepository jpaPriceOfferRepository;

    @Override
    @Transactional
    public void save(PriceOffer priceOffer) {
        jpaPriceOfferRepository.save(PriceOfferMapper.toEntityFrom(priceOffer));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PriceOffer> findById(Long productId, Long companyId) {
        return jpaPriceOfferRepository.findById(new PriceOfferId(productId, companyId))
                .map(PriceOfferMapper::toDomainFrom);
    }

}
