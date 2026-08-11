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
import com.example.marketflow.products.ProductEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartService {
    private final CartItemRepository repost;
    private final ProductRepository rep;
    private final CartitemMapper mapper;

    private CartItemEntity findOwnedCartItem(Long itemId, Long buyerId) {
        return repost.findByIdAndBuyerid(itemId, buyerId).orElseThrow(
                () -> new IllegalArgumentException("Cart item was not found")
        );
    }

    @Transactional
    public void addProductToCart(Long buyerId, Long productId) {
        ProductEntity product = rep.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Product was not found")
        );

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new IllegalArgumentException("Product is not available");
        }

        Optional<CartItemEntity> existingItem =
                repost.findByBuyeridAndProductid(buyerId, productId);

        int newQuantity = existingItem
                .map(item -> item.getQuantity() + 1)
                .orElse(1);

        if (newQuantity > product.getQuantity()) {
            throw new IllegalArgumentException("Not enough product in stock");
        }

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(newQuantity);
        } else {
            repost.save(new CartItemEntity(buyerId, productId));
        }
    }

    @Transactional(readOnly = true)
    public List<CartitemDto> getUserCartItems(Long buyerId) {
        return repost.findAllByBuyerid(buyerId).stream().map(CartitemMapper::convertByEntity).toList();
    }

    @Transactional
    public Integer updateCartItemQuantity(
            Long id,
            Long buyerId,
            Integer quantity
    ) {
        CartItemEntity item = findOwnedCartItem(id, buyerId);
        ProductEntity product = rep.findById(item.getProductid()).orElseThrow();

        int newQuantity = Math.max(1, Math.min(quantity, product.getQuantity()));
        item.setQuantity(newQuantity);
        return newQuantity;
    }

    @Transactional
    public void changeCartItemSelection(
            Long id,
            Long buyerId,
            Boolean active
    ) {
        CartItemEntity item = repost.findByIdAndBuyerid(id, buyerId).orElseThrow();
        item.setSelected(active);
    }

    @Transactional
    public void removeCartItem(Long itemId, Long buyerId) {
        repost.delete(findOwnedCartItem(itemId, buyerId));
    }
}
