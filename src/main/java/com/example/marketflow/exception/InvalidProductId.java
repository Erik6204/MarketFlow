package com.example.marketflow.exception;

public class InvalidProductId extends RuntimeException{
    public InvalidProductId(Long id){
        super("Некоректный id:"+id+" .Он должен быть > 0");
    }

}
