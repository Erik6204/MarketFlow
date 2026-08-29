package com.example.marketflow.Order;

import java.math.BigDecimal;

public record OrderItemDto(
        Long productId,
        Long sellerId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice,
        String imageUrl
) {
}
