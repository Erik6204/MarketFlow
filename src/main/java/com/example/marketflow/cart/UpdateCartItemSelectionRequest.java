package com.example.marketflow.cart;

import jakarta.validation.constraints.NotNull;

public record UpdateCartItemSelectionRequest(
        @NotNull
        Boolean selected
) {
}
