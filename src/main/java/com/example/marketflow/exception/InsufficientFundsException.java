package com.example.marketflow.exception;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(){
        super("денег на карте недостаточно");
    }
}
