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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal(Long buyerId) {
        List<CartItemEntity> cartItems =
                cartItemRepository.findAllByBuyerIdAndSelectedTrue(buyerId);

        if (cartItems.isEmpty()) {
            throw new NoSelectedCartItemsException();
        }

        List<Long> productIds = cartItems.stream()
                .map(CartItemEntity::getProductId)
                .toList();

        Map<Long, ProductEntity> productsById = productRepository.findAllById(productIds)
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

            if (!Boolean.TRUE.equals(product.getActive())
                    || product.getQuantity() == null
                    || product.getQuantity() <= 0) {
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
