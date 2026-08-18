package com.example.marketflow.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id){
        super("Пользователя с таким id:"+id+" нету ");
    }
}
