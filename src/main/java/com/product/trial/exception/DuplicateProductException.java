package com.product.trial.exception;

public class DuplicateProductException extends RuntimeException {
    public DuplicateProductException(Long id) {
        super("Product with ID " + id + " already in wishlist.");
    }
}
