package com.example.marketflow.exception;

public class OwnerWalletAccountNotFoundException extends RuntimeException {

    public OwnerWalletAccountNotFoundException() {
        super("Внутренний счёт владельца платформы не найден");
    }
}
