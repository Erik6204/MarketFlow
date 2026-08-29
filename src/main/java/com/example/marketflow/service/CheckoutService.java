package com.example.marketflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Order.OrderEntity;
import com.example.marketflow.Order.OrderItemEntity;
import com.example.marketflow.Order.OrderStatus;
import com.example.marketflow.Repository.CartItemRepository;
import com.example.marketflow.Repository.OrderItemRepository;
import com.example.marketflow.Repository.OrderRepository;
import com.example.marketflow.Repository.ProductRepository;
import com.example.marketflow.cart.CartItemEntity;
import com.example.marketflow.exception.InsufficientStockException;
import com.example.marketflow.exception.InvalidQuantityException;
import com.example.marketflow.exception.NoSelectedCartItemsException;
import com.example.marketflow.exception.NotEnoughProductQuantityException;
import com.example.marketflow.exception.ProductNotFoundException;
import com.example.marketflow.exception.ProductUnavailableException;
import com.example.marketflow.products.ProductEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final CartItemRepository CIR;
    private final ProductRepository PR;
    private final OrderRepository OR;
    private final OrderItemRepository OIR;

    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal(Long buyerId) {
        List<CartItemEntity> cartItems =
                CIR.findAllByBuyeridAndSelectedTrue(buyerId);

        if (cartItems.isEmpty()) {
            throw new NoSelectedCartItemsException();
        }

        List<Long> productIds = cartItems.stream()
                .map(CartItemEntity::getProductId)
                .toList();

        Map<Long, ProductEntity> productsById = PR.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(
                        ProductEntity::getId,
                        product -> product
                ));

        BigDecimal total = BigDecimal.ZERO;

        for (CartItemEntity item : cartItems) {
            ProductEntity product = productsById.get(item.getProductId());

            if (product == null) {
                throw new ProductNotFoundException(item.getProductId());
            }

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new ProductUnavailableException();
            }

            if (product.getQuantity() < item.getQuantity()) {
                throw new InsufficientStockException();
            }

            BigDecimal itemTotal = product.getPrice().multiply(
                    BigDecimal.valueOf(item.getQuantity().longValue())
            );
            total = total.add(itemTotal);
        }

        return total;
    }
    
    
    @Transactional
    public Long createOrder(Long buyerId) {
        List<CartItemEntity> selectedItems =
                CIR.findAllByBuyeridAndSelectedTrue(buyerId);
        //Находим список всех товаров корзины

        if (selectedItems.isEmpty()) {
            throw new NoSelectedCartItemsException();
        }

        List<Long> productIds = selectedItems.stream()//собираем все id продуктов
                .map(CartItemEntity::getProductId)
                .distinct()
                .toList();

        List<ProductEntity> products = PR.findAllById(productIds);//находим по всем id продуктов их данные 

        Map<Long, ProductEntity> productsById = products.stream()
                .collect(Collectors.toMap(
                        ProductEntity::getId,
                        Function.identity()
                ));

        BigDecimal amount = BigDecimal.ZERO;

        for (CartItemEntity cartItem : selectedItems) {//проверка корзины 
            if (cartItem.getQuantity() == null
                    || cartItem.getQuantity() <= 0) {
                throw new InvalidQuantityException(
                        cartItem.getQuantity()
                );
            }

            ProductEntity product =
                    productsById.get(cartItem.getProductId());
            //Проверка самого продукта
            if (product == null) {
                throw new ProductNotFoundException(
                        cartItem.getProductId()
                );
            }

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new ProductUnavailableException();
            }

            if (product.getQuantity() == null
                    || product.getQuantity() < cartItem.getQuantity()) {
                throw new NotEnoughProductQuantityException();
            }

            BigDecimal itemTotal = product.getPrice()
                    .multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())
                    );

            amount = amount.add(itemTotal);
        }

        OrderEntity savedOrder = OR.save(//Создаем заказ
                new OrderEntity(
                        buyerId,
                        OrderStatus.CREATED,
                        amount
                )
        );

        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (CartItemEntity cartItem : selectedItems) {//Создаем данные подзаказа
            ProductEntity product =
                    productsById.get(cartItem.getProductId());

            OrderItemEntity orderItem = new OrderItemEntity(
                    savedOrder.getId(),
                    product.getId(),
                    product.getSellerId(),
                    product.getName(),
                    product.getPrice(),
                    cartItem.getQuantity(),
                    product.getUrl()
            );

            orderItems.add(orderItem);
        }

        OIR.saveAll(orderItems);

        return savedOrder.getId();
    }
}
