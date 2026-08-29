package com.example.marketflow.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id){
        super("Продукт с таким id:"+ id +" не найден");
    }
}
