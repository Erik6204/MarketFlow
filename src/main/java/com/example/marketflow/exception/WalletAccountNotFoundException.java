package com.example.marketflow.exception;

public class WalletAccountNotFoundException extends RuntimeException{
    public WalletAccountNotFoundException(Long userId){
        super("Внутренний счет пользователя "+userId + " не найден");
    }
}
