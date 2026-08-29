package com.example.marketflow.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException (){
        super("Неверный email или пароль");
    }
    
}
