package com.example.marketflow.Order;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "unit_price", nullable = false)
    @DecimalMin(value = "0.01")
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    @Min(1)
    private Integer quantity;

    @Column(name = "total_price", nullable = false)
    @DecimalMin(value = "0.01")
    private BigDecimal totalPrice;

    @Column(name = "image_url")
    private String imageUrl;

    public OrderItemEntity(
            Long orderId,
            Long productId,
            Long sellerId,
            String productName,
            BigDecimal unitPrice,
            Integer quantity,
            String imageUrl
    ) {
        this.orderId = orderId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.imageUrl = imageUrl;
    }
}
