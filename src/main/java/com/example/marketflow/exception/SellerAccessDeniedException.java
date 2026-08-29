package com.example.marketflow.exception;

public class SellerAccessDeniedException extends RuntimeException {

    public SellerAccessDeniedException() {
        super("Доступ к функциям продавца запрещен");
    }
}
