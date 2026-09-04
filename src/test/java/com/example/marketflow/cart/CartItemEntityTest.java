package com.example.marketflow.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.marketflow.exception.InvalidQuantityException;

class CartItemEntityTest {

    @Test
    void changeQuantityUpdatesEntityForValidValue() {
        CartItemEntity item = new CartItemEntity(7L, 20L);

        item.changeQuantity(3);

        assertEquals(3, item.getQuantity());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {0, -1})
    void changeQuantityRejectsInvalidValueWithoutChangingState(Integer quantity) {
        CartItemEntity item = new CartItemEntity(7L, 20L);

        assertThrows(
                InvalidQuantityException.class,
                () -> item.changeQuantity(quantity)
        );

        assertEquals(1, item.getQuantity());
    }
}
