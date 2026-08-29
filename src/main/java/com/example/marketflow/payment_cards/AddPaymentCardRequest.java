package com.example.marketflow.payment_cards;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class AddPaymentCardRequest {
    
    Long userid;

    String cardtoken;
    String maskedNumber;
    BigDecimal balance;
 
}
