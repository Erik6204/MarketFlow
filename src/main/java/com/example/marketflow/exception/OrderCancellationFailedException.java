package com.example.marketflow.exception;

public class OrderCancellationFailedException extends RuntimeException {

    public OrderCancellationFailedException(Long productId) {
        super("Could not restore stock for product " + productId);
    }
}
