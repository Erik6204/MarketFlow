package com.example.marketflow.exception;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(){
        super("указанная позиция не найдена в корзине пользователя");
    }
}
