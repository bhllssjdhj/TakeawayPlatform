package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppoingCartService {


    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> showCart();

    void cleanCart();

    List<ShoppingCart> subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
