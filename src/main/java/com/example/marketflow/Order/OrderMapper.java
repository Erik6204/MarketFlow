package com.example.marketflow.Order;

import java.util.List;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderItemDto toItemDto(OrderItemEntity item) {
        return new OrderItemDto(
                item.getProductId(),
                item.getSellerId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getTotalPrice(),
                item.getImageUrl()
        );
    }

    public static OrderDetailsDto toDetailsDto(OrderEntity order, List<OrderItemEntity> items) {
        List<OrderItemDto> itemDtos = items.stream()
                .map(OrderMapper::toItemDto)
                .toList();

        return new OrderDetailsDto(
                order.getId(),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                itemDtos
        );
    }
}
