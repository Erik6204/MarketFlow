package com.example.marketflow.exception;

public class NotEnoughProductQuantityException extends RuntimeException{

    public NotEnoughProductQuantityException(){
        super("Недостаточно товара");
    }
    
}
