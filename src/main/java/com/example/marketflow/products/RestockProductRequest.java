package com.example.marketflow.products;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RestockProductRequest(
        @NotNull(message = "Укажите количество пополнения")
        @Min(value = 1, message = "Количество пополнения должно быть больше нуля")
        Integer amount
) {
}
