package com.example.marketflow.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(){
        super("Недостаточно товара на складе");
    }
    
}
