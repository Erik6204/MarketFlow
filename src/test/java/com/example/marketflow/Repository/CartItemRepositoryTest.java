package com.example.marketflow.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.marketflow.cart.CartItemEntity;

import jakarta.persistence.EntityManager;

@DataJpaTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldDeleteOnlySelectedItemsOfSpecifiedBuyer() {
        CartItemEntity selected = new CartItemEntity(7L, 10L);
        CartItemEntity notSelected = new CartItemEntity(7L, 11L);
        notSelected.unselect();
        CartItemEntity anotherBuyer = new CartItemEntity(8L, 12L);

        cartItemRepository.saveAllAndFlush(
                List.of(selected, notSelected, anotherBuyer)
        );

        int deletedRows = cartItemRepository.deleteSelectedByBuyerId(7L);
        entityManager.flush();
        entityManager.clear();

        List<CartItemEntity> buyerItems = cartItemRepository.findAllByBuyerId(7L);
        assertEquals(1, deletedRows);
        assertEquals(1, buyerItems.size());
        assertFalse(buyerItems.get(0).getSelected());
        assertEquals(1, cartItemRepository.findAllByBuyerId(8L).size());
    }
}
