package com.example.marketflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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

import com.example.marketflow.Order.OrderEntity;
import com.example.marketflow.Repository.CartItemRepository;
import com.example.marketflow.Repository.OrderItemRepository;
import com.example.marketflow.Repository.OrderRepository;
import com.example.marketflow.Repository.ProductRepository;
import com.example.marketflow.cart.CartItemEntity;
import com.example.marketflow.exception.InsufficientStockException;
import com.example.marketflow.products.ProductEntity;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderDecreasesStockAndDeletesSelectedCartItems() {
        Long buyerId = 7L;
        Long productId = 11L;
        Long orderId = 21L;

        CartItemEntity cartItem = mock(CartItemEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        OrderEntity savedOrder = mock(OrderEntity.class);

        when(cartItem.getProductId()).thenReturn(productId);
        when(cartItem.getQuantity()).thenReturn(2);
        when(cartItemRepository.findAllByBuyerIdAndSelectedTrue(buyerId))
                .thenReturn(List.of(cartItem));

        when(product.getId()).thenReturn(productId);
        when(product.getSellerId()).thenReturn(5L);
        when(product.getName()).thenReturn("Тестовый товар");
        when(product.getPrice()).thenReturn(new BigDecimal("25.00"));
        when(product.getQuantity()).thenReturn(10);
        when(product.getActive()).thenReturn(true);
        when(product.getUrl()).thenReturn("/images/product.jpg");
        when(productRepository.findAllById(List.of(productId)))
                .thenReturn(List.of(product));
        when(productRepository.decreaseStock(productId, 2)).thenReturn(1);

        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);
        when(savedOrder.getId()).thenReturn(orderId);

        Long result = orderService.createOrder(buyerId);

        assertEquals(orderId, result);
        verify(productRepository).decreaseStock(productId, 2);
        verify(orderItemRepository).saveAll(anyList());
        verify(cartItemRepository).deleteSelectedByBuyerId(buyerId);
    }

    @Test
    void createOrderDoesNotSaveOrderWhenAtomicStockUpdateFails() {
        Long buyerId = 7L;
        Long productId = 11L;

        CartItemEntity cartItem = mock(CartItemEntity.class);
        ProductEntity product = mock(ProductEntity.class);

        when(cartItem.getProductId()).thenReturn(productId);
        when(cartItem.getQuantity()).thenReturn(2);
        when(cartItemRepository.findAllByBuyerIdAndSelectedTrue(buyerId))
                .thenReturn(List.of(cartItem));

        when(product.getId()).thenReturn(productId);
        when(product.getPrice()).thenReturn(new BigDecimal("25.00"));
        when(product.getQuantity()).thenReturn(10);
        when(product.getActive()).thenReturn(true);
        when(productRepository.findAllById(List.of(productId)))
                .thenReturn(List.of(product));
        when(productRepository.decreaseStock(productId, 2)).thenReturn(0);

        assertThrows(
                InsufficientStockException.class,
                () -> orderService.createOrder(buyerId)
        );

        verifyNoInteractions(orderRepository, orderItemRepository);
        verify(cartItemRepository, never()).deleteSelectedByBuyerId(buyerId);
    }
}
