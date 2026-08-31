package com.example.marketflow.exception;

public class InvalidCartItemIdException extends RuntimeException {
    public InvalidCartItemIdException(Long cartItemId) {
        super("Cart item ID must be a positive integer: " + cartItemId);
    }
}
