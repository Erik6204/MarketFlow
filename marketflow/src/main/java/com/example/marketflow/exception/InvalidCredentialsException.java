package com.example.marketflow.exception;

public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException (String message){
        super("Неверные учетные данные:"+message);
    }
    
}
