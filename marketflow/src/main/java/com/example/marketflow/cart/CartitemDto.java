package com.example.marketflow.cart;

import java.math.BigDecimal;

public record CartitemDto(Long id,
        Long productId,
        String productName,
        BigDecimal price,
        Integer quantity,
        Boolean selected,
        String imageUrl,
        BigDecimal subtotal
    ) {
    

}
