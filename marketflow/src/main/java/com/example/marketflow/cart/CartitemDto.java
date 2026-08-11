package com.example.marketflow.cart;

import lombok.Getter;

@Getter
public class CartitemDto {
    private Long buyerid;
    private Long productid;
    private Integer quantity;
    private Boolean selected;
    public CartitemDto(Long buyerid, Long productid) {
        this.buyerid = buyerid;
        this.productid = productid;
        this.quantity = 1;
        this.selected = true;
    }
}
