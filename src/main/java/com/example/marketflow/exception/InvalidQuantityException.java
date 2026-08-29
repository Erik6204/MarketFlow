package com.example.marketflow.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(Integer quanity){
        super("передано недопустимое количество: "+quanity);
    }
}
