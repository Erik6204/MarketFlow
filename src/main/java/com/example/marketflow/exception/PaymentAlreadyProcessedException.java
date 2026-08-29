package com.example.marketflow.exception;

public class PaymentAlreadyProcessedException extends RuntimeException{
    public PaymentAlreadyProcessedException(){
        super("заказ уже оплачен или сейчас обрабатывается");
    }
}
