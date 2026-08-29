package com.example.marketflow.service;



import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Repository.CartItemRepository;
import com.example.marketflow.Repository.ProductRepository;
import com.example.marketflow.cart.CartItemEntity;
import com.example.marketflow.cart.CartitemDto;
import com.example.marketflow.cart.CartitemMapper;
import com.example.marketflow.exception.CartItemNotFoundException;
import com.example.marketflow.exception.InsufficientStockException;
import com.example.marketflow.exception.InvalidQuantityException;
import com.example.marketflow.exception.ProductNotFoundException;
import com.example.marketflow.exception.ProductUnavailableException;
import com.example.marketflow.products.ProductEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartService {
    private final CartItemRepository repost;
    private final ProductRepository rep;
    

    private CartItemEntity findOwnedCartItem(Long itemId, Long buyerId) {
        return repost.findByIdAndBuyerid(itemId, buyerId).orElseThrow(
                () -> new CartItemNotFoundException()
        );
    }

    @Transactional
    public void addProductToCart(Long buyerId, Long productId ) {
        ProductEntity product = rep.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(productId)
        );
        
        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new ProductUnavailableException();
        }

        Optional<CartItemEntity> existingItem =
                repost.findByBuyeridAndProductid(buyerId, productId);

        int newQuantity = existingItem
                .map(item -> item.getQuantity() + 1)
                .orElse(1);

        if (newQuantity > product.getQuantity()) {
            throw new InsufficientStockException();
        }

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(newQuantity);
        } else {
            repost.save(new CartItemEntity(buyerId, productId));
        }
    }

    @Transactional(readOnly = true)
    public List<CartitemDto> getUserCartItems(Long buyerId) {
        return repost.findAllByBuyerid(buyerId)
            .stream()
            .map(item -> {
                ProductEntity product = rep
                        .findById(item.getProductId())
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        item.getProductId()
                                )
                        );

                return CartitemMapper.convertByEntity(
                        item,
                        product
                );
            })
            .toList();
    }

    @Transactional
    public Integer updateCartItemQuantity(
            Long id,
            Long buyerId,
            Integer quantity
    ) {
        if (quantity == null || quantity < 1) {
            throw new InvalidQuantityException(quantity);
        }

        CartItemEntity item = findOwnedCartItem(id, buyerId);

        ProductEntity product = rep.findById(item.getProductId())
                .orElseThrow(
                        () -> new ProductNotFoundException(item.getProductId())
                );

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new ProductUnavailableException();
        }

        if (product.getQuantity() == null || product.getQuantity() <= 0) {
            throw new ProductUnavailableException();
        }

        if (quantity > product.getQuantity()) {
            throw new InsufficientStockException();
        }

        item.setQuantity(quantity);

        return quantity;
    }

    @Transactional
    public void changeCartItemSelection(
            Long itemId,
            Long buyerId,
            boolean active
    ) {
        CartItemEntity item = findOwnedCartItem(itemId, buyerId);
        item.setSelected(active);
    }

    @Transactional
    public void removeCartItem(Long itemId, Long buyerId) {
        repost.delete(findOwnedCartItem(itemId, buyerId));
    }
}
