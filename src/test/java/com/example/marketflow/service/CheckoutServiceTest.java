package com.example.marketflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.marketflow.Repository.CartItemRepository;
import com.example.marketflow.Repository.ProductRepository;
import com.example.marketflow.cart.CartItemEntity;
import com.example.marketflow.exception.InsufficientStockException;
import com.example.marketflow.exception.NoSelectedCartItemsException;
import com.example.marketflow.products.ProductEntity;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    void calculateCartTotalSumsSelectedItems() {
        CartItemEntity firstItem = new CartItemEntity(7L, 10L);
        firstItem.setQuantity(2);
        CartItemEntity secondItem = new CartItemEntity(7L, 11L);
        secondItem.setQuantity(3);

        ProductEntity firstProduct = product(10L, "100.00", 10);
        ProductEntity secondProduct = product(11L, "50.00", 10);

        when(cartItemRepository.findAllByBuyerIdAndSelectedTrue(7L))
                .thenReturn(List.of(firstItem, secondItem));
        when(productRepository.findAllById(List.of(10L, 11L)))
                .thenReturn(List.of(firstProduct, secondProduct));

        BigDecimal result = checkoutService.calculateCartTotal(7L);

        assertEquals(0, new BigDecimal("350.00").compareTo(result));
        verify(productRepository).findAllById(List.of(10L, 11L));
    }

    @Test
    void calculateCartTotalRejectsEmptySelectionBeforeLoadingProducts() {
        when(cartItemRepository.findAllByBuyerIdAndSelectedTrue(7L))
                .thenReturn(List.of());

        assertThrows(
                NoSelectedCartItemsException.class,
                () -> checkoutService.calculateCartTotal(7L)
        );

        verifyNoInteractions(productRepository);
    }

    @Test
    void calculateCartTotalRejectsQuantityGreaterThanStock() {
        CartItemEntity item = new CartItemEntity(7L, 10L);
        item.setQuantity(3);
        ProductEntity product = product(10L, "100.00", 2);

        when(cartItemRepository.findAllByBuyerIdAndSelectedTrue(7L))
                .thenReturn(List.of(item));
        when(productRepository.findAllById(List.of(10L)))
                .thenReturn(List.of(product));

        assertThrows(
                InsufficientStockException.class,
                () -> checkoutService.calculateCartTotal(7L)
        );
    }

    private ProductEntity product(Long id, String price, int quantity) {
        ProductEntity product = new ProductEntity(
                3L,
                "Product " + id,
                "Description",
                new BigDecimal(price),
                quantity,
                "product.jpg"
        );
        product.setId(id);
        product.setActive(true);
        return product;
    }
}
