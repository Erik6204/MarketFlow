package com.example.marketflow.products;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank(message = "Название товара не должно быть пустым")
        @Size(max = 200, message = "Название товара не должно быть длиннее 200 символов")
        String name,

        String description,

        @NotNull(message = "Цена обязательна")
        @DecimalMin(value = "0.01", message = "Цена должна быть больше нуля")
        BigDecimal price,

        @NotNull(message = "Количество обязательно")
        @Min(value = 0, message = "Количество не может быть отрицательным")
        Integer quantity,

        @Size(max = 1000, message = "Ссылка на изображение не должна быть длиннее 1000 символов")
        String imageUrl
) {
}
