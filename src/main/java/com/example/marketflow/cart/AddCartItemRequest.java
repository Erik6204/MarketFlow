package com.example.marketflow.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(
        @NotNull
        @Min(1)
        Long productId
) {
}
