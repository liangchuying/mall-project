package com.mall.controller;

import com.mall.annotation.Idempotent;
import com.mall.utils.IdempotentUtil;
import com.mall.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "幂等性测试", description = "接口幂等性测试接口")
@RestController
@RequestMapping("/idempotent")
public class IdempotentController {

    @Autowired
    private IdempotentUtil idempotentUtil;

    @Operation(summary = "获取幂等性 Token", description = "客户端在请求需要幂等性的接口前，先调用此接口获取 Token")
    @GetMapping("/token")
    public Result<String> getToken() {
        String token = idempotentUtil.createToken();
        return Result.success(token);
    }

    @Operation(summary = "测试幂等性接口（注解方式）", description = "使用 @Idempotent 注解实现，需要在请求头携带 Idempotent-Token")
    @PostMapping("/test")
    @Idempotent(expireTime = 60, info = "请勿重复提交，请求正在处理中")
    public Result<String> testIdempotent(
            @Parameter(description = "测试数据", required = true) @RequestParam String data,
            @Parameter(description = "幂等性 Token", required = true) @RequestHeader("Idempotent-Token") String token
    ) {
        return Result.success("处理成功: " + data + "，时间: " + System.currentTimeMillis());
    }

    @Operation(summary = "模拟创建订单", description = "演示订单创建的幂等性控制")
    @PostMapping("/order")
    @Idempotent(expireTime = 120, info = "订单正在创建中，请勿重复提交")
    public Result<String> createOrder(
            @Parameter(description = "商品ID", required = true) @RequestParam Long productId,
            @Parameter(description = "数量", required = true) @RequestParam Integer quantity,
            @RequestHeader("Idempotent-Token") String token
    ) {
        return Result.success("订单创建成功，订单号: " + System.currentTimeMillis());
    }
}
