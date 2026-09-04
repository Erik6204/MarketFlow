package com.example.marketflow.exception;

public class InvalidOrderIdException extends RuntimeException {

    public InvalidOrderIdException(Long orderId) {
        super("Order ID must be a positive integer: " + orderId);
    }
}
