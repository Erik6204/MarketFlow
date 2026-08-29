package com.example.marketflow.exception;

public class PaymentCardNotFoundException extends RuntimeException{
    public PaymentCardNotFoundException(){
        super("карта не существует, неактивна или принадлежит другому пользователю\n" );
    }
}
