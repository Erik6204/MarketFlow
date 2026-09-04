package com.example.marketflow.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.marketflow.Repository.CartItemRepository;
import com.example.marketflow.Repository.ProductRepository;
import com.example.marketflow.cart.CartItemEntity;
import com.example.marketflow.exception.InsufficientStockException;
import com.example.marketflow.exception.ProductUnavailableException;
import com.example.marketflow.products.ProductEntity;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldAddNewProductToCart(){
        Long buyerId = 5L;
        Long productId = 20L;


        ProductEntity product = new ProductEntity(
                10L,
                "Mechanical keyboard",
                "Keyboard with backlight",
                new BigDecimal("1000.00"),
                15,
                "keyboard.jpg"
        );

        product.setId(productId);
        product.setActive(true);


        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        when(cartItemRepository.findByBuyerIdAndProductId(buyerId, productId)).thenReturn(Optional.empty());

        cartService.addProductToCart(buyerId, productId);

        ArgumentCaptor<CartItemEntity> captor=ArgumentCaptor.forClass(CartItemEntity.class);

        verify(cartItemRepository).save(captor.capture());

        CartItemEntity entity=captor.getValue();

        assertEquals(buyerId,entity.getBuyerId());
        assertEquals(productId,entity.getProductId());
        assertEquals(1,entity.getQuantity());
        assertEquals(true,entity.getSelected());

        verify(productRepository).findById(productId);

        verify(cartItemRepository).findByBuyerIdAndProductId(buyerId, productId);
        
       
    } 


    @Test
    void shouldIncreaseQuantityWhenProductAlreadyExistsInCart() {
        
        Long buyerId = 5L;
        Long productId = 20L;

        ProductEntity product = new ProductEntity(
                10L,
                "Mechanical keyboard",
                "Keyboard with backlight",
                new BigDecimal("1000.00"),
                15,
                "keyboard.jpg"
        );

        product.setId(productId);
        product.setActive(true);

        CartItemEntity existingItem =
                new CartItemEntity(buyerId, productId);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(cartItemRepository.findByBuyerIdAndProductId(
                buyerId,
                productId
        )).thenReturn(Optional.of(existingItem));

        
        cartService.addProductToCart(buyerId, productId);

        assertEquals(2, existingItem.getQuantity());

        verify(productRepository).findById(productId);

        verify(cartItemRepository)
                .findByBuyerIdAndProductId(buyerId, productId);

        verify(cartItemRepository, never())
                .save(any(CartItemEntity.class));
    }

    @Test
    void shouldRejectUnavailableProductBeforeReadingCart() {
        ProductEntity product = new ProductEntity(
                10L,
                "Keyboard",
                "Description",
                new BigDecimal("1000.00"),
                10,
                "keyboard.jpg"
        );
        product.setId(20L);
        product.setActive(false);
        when(productRepository.findById(20L)).thenReturn(Optional.of(product));

        assertThrows(
                ProductUnavailableException.class,
                () -> cartService.addProductToCart(5L, 20L)
        );

        verifyNoInteractions(cartItemRepository);
    }

    @Test
    void shouldNotChangeQuantityWhenRequestedQuantityExceedsStock() {
        CartItemEntity item = new CartItemEntity(5L, 20L);
        ProductEntity product = new ProductEntity(
                10L,
                "Keyboard",
                "Description",
                new BigDecimal("1000.00"),
                2,
                "keyboard.jpg"
        );
        product.setId(20L);
        product.setActive(true);

        when(cartItemRepository.findByIdAndBuyerId(25L, 5L))
                .thenReturn(Optional.of(item));
        when(productRepository.findById(20L)).thenReturn(Optional.of(product));

        assertThrows(
                InsufficientStockException.class,
                () -> cartService.updateCartItemQuantity(25L, 5L, 3)
        );

        assertEquals(1, item.getQuantity());
    }

    @Test
    void shouldDeleteOnlyCartItemOwnedByBuyer() {
        CartItemEntity item = new CartItemEntity(5L, 20L);
        when(cartItemRepository.findByIdAndBuyerId(25L, 5L))
                .thenReturn(Optional.of(item));

        cartService.removeCartItem(25L, 5L);

        verify(cartItemRepository).delete(item);
    }
}
