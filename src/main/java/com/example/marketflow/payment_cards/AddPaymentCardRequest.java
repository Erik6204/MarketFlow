package com.example.marketflow.payment_cards;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class AddPaymentCardRequest {

    @NotBlank(message = "Card token is required")
    @Size(max = 100, message = "Card token must not be longer than 100 characters")
    String cardtoken;

    @NotBlank(message = "Masked card number is required")
    @Size(max = 30, message = "Masked card number must not be longer than 30 characters")
    String maskedNumber;

    @NotNull(message = "Card balance is required")
    @DecimalMin(value = "0.00", message = "Card balance must not be negative")
    BigDecimal balance;
}
