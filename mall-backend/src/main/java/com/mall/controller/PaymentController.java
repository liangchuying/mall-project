package com.mall.controller;

import com.mall.dto.PaymentCreateDTO;
import com.mall.service.PaymentService;
import com.mall.utils.JwtUtil;
import com.mall.utils.Result;
import com.mall.vo.PaymentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "支付管理", description = "支付相关接口")
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

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

    @Operation(summary = "创建支付", description = "创建支付记录")
    @PostMapping
    public Result<PaymentVO> createPayment(
            HttpServletRequest request,
            @Valid @RequestBody PaymentCreateDTO dto
    ) {
        Long userId = getUserId(request);
        PaymentVO payment = paymentService.createPayment(userId, dto);
        return Result.success(payment);
    }

    @Operation(summary = "获取支付链接", description = "获取第三方支付链接")
    @GetMapping("/url/{paymentNo}")
    public Result<String> getPaymentUrl(
            HttpServletRequest request,
            @Parameter(description = "支付流水号", required = true) @PathVariable String paymentNo
    ) {
        Long userId = getUserId(request);
        String url = paymentService.getPaymentUrl(userId, paymentNo);
        return Result.success(url);
    }

    @Operation(summary = "获取支付记录", description = "根据支付流水号获取支付记录")
    @GetMapping("/{paymentNo}")
    public Result<PaymentVO> getPaymentByNo(
            HttpServletRequest request,
            @Parameter(description = "支付流水号", required = true) @PathVariable String paymentNo
    ) {
        Long userId = getUserId(request);
        PaymentVO payment = paymentService.getPaymentByNo(userId, paymentNo);
        return Result.success(payment);
    }

    @Operation(summary = "根据订单号获取支付记录", description = "根据订单号获取支付记录")
    @GetMapping("/order/{orderNo}")
    public Result<PaymentVO> getPaymentByOrderNo(
            HttpServletRequest request,
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo
    ) {
        Long userId = getUserId(request);
        PaymentVO payment = paymentService.getPaymentByOrderNo(userId, orderNo);
        return Result.success(payment);
    }

    @Operation(summary = "退款", description = "申请退款")
    @PostMapping("/refund/{paymentNo}")
    public Result<Void> refundPayment(
            HttpServletRequest request,
            @Parameter(description = "支付流水号", required = true) @PathVariable String paymentNo,
            @Parameter(description = "退款原因") @RequestParam String refundReason
    ) {
        Long userId = getUserId(request);
        paymentService.refundPayment(userId, paymentNo, refundReason);
        return Result.success();
    }

    @Operation(summary = "支付宝支付回调", description = "支付宝异步通知回调")
    @PostMapping("/notify/alipay")
    public String alipayNotify(@RequestBody Map<String, String> params) {
        boolean success = paymentService.handlePaymentNotify("ALIPAY", params);
        return success ? "success" : "fail";
    }

    @Operation(summary = "微信支付回调", description = "微信支付异步通知回调")
    @PostMapping("/notify/wechat")
    public String wechatNotify(@RequestBody Map<String, String> params) {
        boolean success = paymentService.handlePaymentNotify("WECHAT", params);
        return success ? "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>" : "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[处理失败]]></return_msg></xml>";
    }
}
