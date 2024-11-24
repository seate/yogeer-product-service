package com.yoger.productserviceorganization.priceOffer.domain.exception;

public class PriceOfferAlreadyExistException extends RuntimeException {
    public PriceOfferAlreadyExistException(String message) {
        super(message);
    }
}
