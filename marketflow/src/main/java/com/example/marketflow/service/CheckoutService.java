package com.example.marketflow.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Repository.CartItemRepository;
import com.example.marketflow.Repository.ProductRepository;
import com.example.marketflow.cart.CartItemEntity;
import com.example.marketflow.exception.InsufficientStockException;
import com.example.marketflow.exception.NoSelectedCartItemsException;
import com.example.marketflow.exception.ProductNotFoundException;
import com.example.marketflow.exception.ProductUnavailableException;
import com.example.marketflow.products.ProductEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final CartItemRepository repost;
    private final ProductRepository rep;

    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal(Long buyerId) {
        List<CartItemEntity> cartItems =
                repost.findAllByBuyeridAndSelectedTrue(buyerId);

        if (cartItems.isEmpty()) {
            throw new NoSelectedCartItemsException();
        }

        List<Long> productIds = cartItems.stream()
                .map(CartItemEntity::getProductid)
                .toList();

        Map<Long, ProductEntity> productsById = rep.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(
                        ProductEntity::getId,
                        product -> product
                ));

        BigDecimal total = BigDecimal.ZERO;

        for (CartItemEntity item : cartItems) {
            ProductEntity product = productsById.get(item.getProductid());

            if (product == null) {
                throw new ProductNotFoundException(item.getProductid());
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
}
