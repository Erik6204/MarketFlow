package com.example.marketflow.exception;

public class ProductUnavailableException extends RuntimeException{
    public ProductUnavailableException(){
        super("товар существует, но сейчас недоступен для покупки");
    }
}
