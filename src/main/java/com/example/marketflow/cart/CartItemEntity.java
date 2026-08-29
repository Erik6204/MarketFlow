package com.example.marketflow.cart;

import java.time.Instant;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import com.example.marketflow.exception.InvalidQuantityException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Positive
    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Boolean selected;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public CartItemEntity(Long buyerId, Long productId) {
        this.buyerId = Objects.requireNonNull(buyerId);
        this.productId = Objects.requireNonNull(productId);
        this.quantity = 1;
        this.selected = true;
    }

    public void changeQuantity(Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new InvalidQuantityException(quantity);
        }

        this.quantity = quantity;
    }

    public void select() {
        this.selected = true;
    }

    public void unselect() {
        this.selected = false;
    }
}
