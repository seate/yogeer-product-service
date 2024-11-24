package com.yoger.productserviceorganization.priceOffer.adapters.web.controller;

import com.yoger.productserviceorganization.priceOffer.adapters.web.dto.request.PriceOfferRequestDTO;
import com.yoger.productserviceorganization.priceOffer.adapters.web.dto.response.PriceOffersResponseDTO;
import com.yoger.productserviceorganization.priceOffer.application.PriceOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/price-offers")
@RequiredArgsConstructor
public class PriceOfferController {
    private final PriceOfferService priceOfferService;
    
    @PostMapping("/{productId}")
    public ResponseEntity<Void> offerPrices(@PathVariable Long productId,
                                            @RequestHeader("user-id") Long companyId, //TODO header를 companyId로 변경
                                            @RequestBody @Valid PriceOfferRequestDTO priceOfferRequestDTO) {
        priceOfferService.create(productId, companyId, priceOfferRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<PriceOffersResponseDTO> getPriceOffersByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(priceOfferService.getAllByProductId(productId), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId,
                                       @RequestHeader("user-id") Long companyId) {
        priceOfferService.delete(productId, companyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
