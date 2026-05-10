package com.mall.controller;

import com.mall.dto.CartAddDTO;
import com.mall.dto.CartUpdateDTO;
import com.mall.service.CartService;
import com.mall.utils.JwtUtil;
import com.mall.utils.Result;
import com.mall.vo.CartVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "购物车管理", description = "购物车相关接口")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtUtil jwtUtil;

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("未登录或token无效");
        }
        token = token.substring(7);
        return jwtUtil.getUserId(token);
    }

    @Operation(summary = "添加到购物车", description = "添加商品到购物车，如果商品已存在则增加数量")
    @PostMapping
    public Result<Long> addToCart(
            HttpServletRequest request,
            @Valid @RequestBody CartAddDTO dto
    ) {
        Long userId = getUserId(request);
        Long cartId = cartService.addToCart(userId, dto);
        return Result.success(cartId);
    }

    @Operation(summary = "更新购物车", description = "更新购物车商品数量或选中状态")
    @PutMapping
    public Result<Void> updateCart(
            HttpServletRequest request,
            @Valid @RequestBody CartUpdateDTO dto
    ) {
        Long userId = getUserId(request);
        cartService.updateCart(userId, dto);
        return Result.success();
    }

    @Operation(summary = "删除购物车商品", description = "根据购物车ID删除商品")
    @DeleteMapping("/{cartId}")
    public Result<Void> deleteCart(
            HttpServletRequest request,
            @Parameter(description = "购物车ID", required = true) @PathVariable Long cartId
    ) {
        Long userId = getUserId(request);
        cartService.deleteCart(userId, cartId);
        return Result.success();
    }

    @Operation(summary = "批量删除购物车商品", description = "根据购物车ID列表批量删除商品")
    @DeleteMapping("/batch")
    public Result<Void> deleteCartBatch(
            HttpServletRequest request,
            @RequestBody List<Long> cartIds
    ) {
        Long userId = getUserId(request);
        cartService.deleteCartBatch(userId, cartIds);
        return Result.success();
    }

    @Operation(summary = "获取购物车列表", description = "获取当前用户的购物车列表")
    @GetMapping
    public Result<List<CartVO>> getCartList(HttpServletRequest request) {
        Long userId = getUserId(request);
        List<CartVO> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }

    @Operation(summary = "全选/取消全选", description = "全选或取消全选购物车商品")
    @PutMapping("/select-all")
    public Result<Void> selectAll(
            HttpServletRequest request,
            @Parameter(description = "是否选中：true=全选，false=取消全选", required = true) @RequestParam Boolean selected
    ) {
        Long userId = getUserId(request);
        cartService.selectAll(userId, selected);
        return Result.success();
    }

    @Operation(summary = "获取购物车总价", description = "获取当前用户选中商品的总价")
    @GetMapping("/total-price")
    public Result<BigDecimal> getCartTotalPrice(HttpServletRequest request) {
        Long userId = getUserId(request);
        BigDecimal totalPrice = cartService.getCartTotalPrice(userId);
        return Result.success(totalPrice);
    }

    @Operation(summary = "获取购物车商品数量", description = "获取当前用户选中商品的总数量")
    @GetMapping("/count")
    public Result<Integer> getCartCount(HttpServletRequest request) {
        Long userId = getUserId(request);
        Integer count = cartService.getCartCount(userId);
        return Result.success(count);
    }
}
