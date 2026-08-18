package com.example.marketflow.products;

import java.math.BigDecimal;

public record SellerProductDto(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer quantity,
    String imageUrl,
    BigDecimal costPrice,
    Boolean active) 
{}
