package com.example.marketflow.products;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductRequest {
    @Size(max = 200, message = "Название товара не должно быть длиннее 200 символов")
    String name;
    String description;
    @DecimalMin(value = "0.01", message = "Цена должна быть больше нуля")
    BigDecimal price;
    @Size(max = 1000, message = "Ссылка на изображение не должна быть длиннее 1000 символов")
    String imageUrl;
}
