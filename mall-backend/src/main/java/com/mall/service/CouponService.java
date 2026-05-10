package com.mall.service;

import com.mall.vo.CouponVO;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {

    Long createCoupon(String name, Integer type, BigDecimal discountAmount, BigDecimal discountRate, BigDecimal minAmount, Integer totalCount, Integer validDays);

    List<CouponVO> getAvailableCoupons(Long userId);

    String receiveCoupon(Long userId, Long couponId);

    BigDecimal calculateDiscount(Long userId, Long couponId, BigDecimal orderAmount, String orderNo);

    List<CouponVO> getUserCoupons(Long userId, Integer status);
}
