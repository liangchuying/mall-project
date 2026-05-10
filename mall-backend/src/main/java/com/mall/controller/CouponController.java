package com.mall.controller;

import com.mall.service.CouponService;
import com.mall.utils.JwtUtil;
import com.mall.utils.Result;
import com.mall.vo.CouponVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "优惠券管理", description = "优惠券相关接口")
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

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

    @Operation(summary = "获取可用优惠券列表", description = "获取当前用户可领取的优惠券列表")
    @GetMapping("/available")
    public Result<List<CouponVO>> getAvailableCoupons(HttpServletRequest request) {
        Long userId = getUserId(request);
        List<CouponVO> coupons = couponService.getAvailableCoupons(userId);
        return Result.success(coupons);
    }

    @Operation(summary = "领取优惠券", description = "用户领取优惠券")
    @PostMapping("/receive/{couponId}")
    public Result<String> receiveCoupon(
            HttpServletRequest request,
            @Parameter(description = "优惠券ID", required = true) @PathVariable Long couponId
    ) {
        Long userId = getUserId(request);
        String couponCode = couponService.receiveCoupon(userId, couponId);
        return Result.success(couponCode);
    }

    @Operation(summary = "获取我的优惠券", description = "获取当前用户的优惠券列表")
    @GetMapping("/my")
    public Result<List<CouponVO>> getMyCoupons(
            HttpServletRequest request,
            @Parameter(description = "优惠券状态：0=未使用，1=已使用，2=已过期") @RequestParam(required = false) Integer status
    ) {
        Long userId = getUserId(request);
        List<CouponVO> coupons = couponService.getUserCoupons(userId, status);
        return Result.success(coupons);
    }

    @Operation(summary = "计算优惠券折扣", description = "计算使用优惠券后的折扣金额")
    @PostMapping("/calculate")
    public Result<BigDecimal> calculateDiscount(
            HttpServletRequest request,
            @Parameter(description = "优惠券ID") @RequestParam(required = false) Long couponId,
            @Parameter(description = "订单金额", required = true) @RequestParam BigDecimal orderAmount,
            @Parameter(description = "订单号", required = true) @RequestParam String orderNo
    ) {
        Long userId = getUserId(request);
        BigDecimal discount = couponService.calculateDiscount(userId, couponId, orderAmount, orderNo);
        return Result.success(discount);
    }
}
