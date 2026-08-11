package com.example.marketflow.cart;

public class CartitemMapper {
    private CartitemMapper(){}

    public static CartitemDto convertByEntity(CartItemEntity entit){
        return new CartitemDto(entit.getBuyerid(),entit.getProductid());
    }

}
