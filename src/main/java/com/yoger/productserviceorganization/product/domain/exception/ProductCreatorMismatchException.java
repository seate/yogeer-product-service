package com.yoger.productserviceorganization.product.domain.exception;

public class ProductCreatorMismatchException extends IllegalArgumentException {
    public ProductCreatorMismatchException(String message) {
        super(message);
    }
}
