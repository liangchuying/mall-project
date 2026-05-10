package com.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.dto.OrderCreateDTO;
import com.mall.service.OrderService;
import com.mall.utils.JwtUtil;
import com.mall.utils.Result;
import com.mall.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

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

    @Operation(summary = "创建订单", description = "根据购物车选中商品创建订单")
    @PostMapping
    public Result<OrderVO> createOrder(
            HttpServletRequest request,
            @Valid @RequestBody OrderCreateDTO dto
    ) {
        Long userId = getUserId(request);
        OrderVO order = orderService.createOrder(userId, dto);
        return Result.success(order);
    }

    @Operation(summary = "支付订单", description = "支付订单并扣减库存")
    @PostMapping("/pay/{orderNo}")
    public Result<OrderVO> payOrder(
            HttpServletRequest request,
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo
    ) {
        Long userId = getUserId(request);
        OrderVO order = orderService.payOrder(userId, orderNo);
        return Result.success(order);
    }

    @Operation(summary = "取消订单", description = "取消订单并解锁库存")
    @PostMapping("/cancel/{orderNo}")
    public Result<OrderVO> cancelOrder(
            HttpServletRequest request,
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo
    ) {
        Long userId = getUserId(request);
        OrderVO order = orderService.cancelOrder(userId, orderNo);
        return Result.success(order);
    }

    @Operation(summary = "发货", description = "商家发货（管理员操作）")
    @PostMapping("/ship/{orderNo}")
    public Result<OrderVO> shipOrder(
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo
    ) {
        OrderVO order = orderService.shipOrder(orderNo);
        return Result.success(order);
    }

    @Operation(summary = "确认收货", description = "用户确认收货")
    @PostMapping("/receive/{orderNo}")
    public Result<OrderVO> receiveOrder(
            HttpServletRequest request,
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo
    ) {
        Long userId = getUserId(request);
        OrderVO order = orderService.receiveOrder(userId, orderNo);
        return Result.success(order);
    }

    @Operation(summary = "完成订单", description = "系统自动完成订单")
    @PostMapping("/complete/{orderNo}")
    public Result<OrderVO> completeOrder(
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo
    ) {
        OrderVO order = orderService.completeOrder(orderNo);
        return Result.success(order);
    }

    @Operation(summary = "获取订单详情", description = "根据订单号获取订单详情")
    @GetMapping("/{orderNo}")
    public Result<OrderVO> getOrderByNo(
            HttpServletRequest request,
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo
    ) {
        Long userId = getUserId(request);
        OrderVO order = orderService.getOrderByNo(userId, orderNo);
        return Result.success(order);
    }

    @Operation(summary = "获取我的订单列表", description = "分页获取当前用户的订单列表")
    @GetMapping("/my")
    public Result<Page<OrderVO>> getMyOrders(
            HttpServletRequest request,
            @Parameter(description = "订单状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size
    ) {
        Long userId = getUserId(request);
        Page<OrderVO> page = new Page<>(current, size);
        Page<OrderVO> result = orderService.getUserOrders(userId, status, page);
        return Result.success(result);
    }

    @Operation(summary = "获取订单详情", description = "根据订单ID获取订单详情")
    @GetMapping("/detail/{orderId}")
    public Result<OrderVO> getOrderDetail(
            HttpServletRequest request,
            @Parameter(description = "订单ID", required = true) @PathVariable Long orderId
    ) {
        Long userId = getUserId(request);
        OrderVO order = orderService.getOrderDetail(userId, orderId);
        return Result.success(order);
    }
}
