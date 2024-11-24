package com.yoger.productserviceorganization.priceOffer.application;

import com.yoger.productserviceorganization.priceOffer.adapters.web.dto.request.PriceOfferRequestDTO;
import com.yoger.productserviceorganization.priceOffer.adapters.web.dto.response.PriceOffersResponseDTO;
import com.yoger.productserviceorganization.priceOffer.domain.exception.PriceOfferAlreadyExistException;
import com.yoger.productserviceorganization.priceOffer.domain.exception.PriceOfferNotAllowedCreateOrUpdateException;
import com.yoger.productserviceorganization.priceOffer.domain.model.PriceOffer;
import com.yoger.productserviceorganization.priceOffer.domain.port.PriceOfferRepository;
import com.yoger.productserviceorganization.priceOffer.mapper.PriceOfferMapper;
import com.yoger.productserviceorganization.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceOfferServiceImpl implements PriceOfferService {
    private final PriceOfferRepository priceOfferRepository;
    private final ProductService productService;

    @Override
    public void create(Long productId, Long companyId, PriceOfferRequestDTO priceOfferRequestDTO) {
        if (isExist(productId, companyId)) {
            throw new PriceOfferAlreadyExistException("이미 존재하는 가격 정보가 있습니다.");
        }

        PriceOffer priceOffer = PriceOfferMapper.createTemporary(productId, companyId, priceOfferRequestDTO.priceByQuantities());
        priceOfferRepository.save(priceOffer);
    }

    private Boolean isExist(Long productId, Long companyId) {
        return priceOfferRepository.findById(productId, companyId).isPresent();
    }

    @Override
    public PriceOffersResponseDTO getAllByProductId(Long productId) {
        return PriceOffersResponseDTO.from(priceOfferRepository.findAllByProductId(productId));
    }

    @Override
    public void update(Long productId, Long companyId, PriceOfferRequestDTO priceOfferRequestDTO) {
        validateUpdatable(productId);

        PriceOffer priceOffer = PriceOfferMapper.createTemporary(productId, companyId, priceOfferRequestDTO.priceByQuantities());
        priceOfferRepository.save(priceOffer);
    }

    @Override
    public void delete(Long productId, Long companyId) {
        validateUpdatable(productId);
        priceOfferRepository.delete(productId, companyId);
    }

    private void validateUpdatable(Long productId) {
        if (!productService.isDemoProduct(productId)) {
            throw new PriceOfferNotAllowedCreateOrUpdateException("업데이트할 수 있는 상품이 아닙니다.");
        }
    }
}
