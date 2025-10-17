package com.cosmocats.marketplace.exception;

public class ProductNotFoundException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "Product with id %d not found";

    public ProductNotFoundException(Long productId) {
        super(String.format(MESSAGE_TEMPLATE, productId));
    }
}