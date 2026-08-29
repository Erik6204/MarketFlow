package com.example.marketflow.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketflow.cart.CartItemEntity;

@Repository
public  interface CartItemRepository extends JpaRepository<CartItemEntity,Long> {
    List<CartItemEntity> findAllByBuyerid(Long buyerId);
    Optional<CartItemEntity> findByIdAndBuyerid(Long id,Long buyerid);
    List<CartItemEntity> findAllByBuyeridAndSelectedTrue(Long buyerid);
    Optional<CartItemEntity> findByBuyeridAndProductid(Long buyerid,Long productid);
}
