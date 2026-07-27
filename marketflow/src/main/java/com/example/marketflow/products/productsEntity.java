package com.example.marketflow.products;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="products")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class productsEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="seller_id")
    private Long md;

    @Column
    private String name;

    @Column
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column
    private Integer quantity;

    @Column(name="image_url")
    private String url;

    @Column
    private Boolean active;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public productsEntity (String name,BigDecimal price,Integer quantity,String url){
        this.name=name;
        this.price=price;
        this.quantity=quantity;
        this.url=url;
    }



}
