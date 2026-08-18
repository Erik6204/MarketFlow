package com.example.marketflow.cart;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name="cart_items")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(name="buyer_id")
    private Long buyerid;
    @NonNull
    @Column(name="product_id")
    private Long productid;
    @NonNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @NonNull
    @Column
    private Boolean selected;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public CartItemEntity(Long buyerid, Long productid) {
        this.buyerid = buyerid;
        this.productid = productid;
        this.quantity = 1;
        this.selected = true;
    }
}
