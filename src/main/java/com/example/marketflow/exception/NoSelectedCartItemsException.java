package com.example.marketflow.exception;

public class NoSelectedCartItemsException extends RuntimeException {

    public NoSelectedCartItemsException() {
        super("Не выбран ни один товар");
    }
}
