package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppoingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Api("用户购物车")
@Slf4j
public class ShoppingCartController {

    @Autowired
    ShoppoingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车:{}",shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> list = shoppingCartService.showCart();
        return Result.success(list);
    }

    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        shoppingCartService.cleanCart();
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation("删除购物车中一件商品")
    public Result<List<ShoppingCart>> deleteOne(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }

}
