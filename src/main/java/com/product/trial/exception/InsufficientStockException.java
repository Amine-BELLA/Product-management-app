package com.product.trial.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long productId) {
        super("Not enough stock available for product with ID: " + productId);
    }
}
