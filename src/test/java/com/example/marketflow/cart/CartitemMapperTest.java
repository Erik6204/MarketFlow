package com.example.marketflow.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.marketflow.products.ProductEntity;

class CartitemMapperTest {

    @Test
    void convertCalculatesSubtotalFromPriceAndQuantity() {
        CartItemEntity item = new CartItemEntity(7L, 20L);
        item.setQuantity(3);
        ProductEntity product = new ProductEntity(
                5L,
                "Keyboard",
                "Mechanical keyboard",
                new BigDecimal("1000.00"),
                10,
                "keyboard.jpg"
        );
        product.setId(20L);

        CartitemDto result = CartitemMapper.convertByEntity(item, product);

        assertEquals(20L, result.productId());
        assertEquals(3, result.quantity());
        assertEquals(0, new BigDecimal("3000.00").compareTo(result.subtotal()));
    }
}
