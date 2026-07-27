package com.example.marketflow.payment_cards;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public class cartdto {
    
    @NonNull
    Long userid;

    @NonNull
    String cardtoken;
    @NonNull
    String maskedNumber;
    @NonNull
    BigDecimal balance;
 
}
