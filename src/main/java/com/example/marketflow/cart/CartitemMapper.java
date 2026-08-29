package com.example.marketflow.cart;

import java.math.BigDecimal;

import com.example.marketflow.products.ProductEntity;

public final class CartitemMapper {

    private CartitemMapper() {
    }

    public static CartitemDto convertByEntity(
            CartItemEntity entity,
            ProductEntity product
    ) {
        BigDecimal subtotal = product.getPrice().multiply(
                BigDecimal.valueOf(entity.getQuantity())
        );

        return new CartitemDto(
                entity.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                entity.getQuantity(),
                entity.getSelected(),
                product.getUrl(),
                subtotal
        );
    }
}