package com.mall.service;

import com.mall.dto.CartAddDTO;
import com.mall.dto.CartUpdateDTO;
import com.mall.vo.CartVO;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    Long addToCart(Long userId, CartAddDTO dto);

    void updateCart(Long userId, CartUpdateDTO dto);

    void deleteCart(Long userId, Long cartId);

    void deleteCartBatch(Long userId, List<Long> cartIds);

    List<CartVO> getCartList(Long userId);

    void selectAll(Long userId, Boolean selected);

    BigDecimal getCartTotalPrice(Long userId);

    Integer getCartCount(Long userId);
}
