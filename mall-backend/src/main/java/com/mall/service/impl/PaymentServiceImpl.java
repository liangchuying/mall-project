package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dto.PaymentCreateDTO;
import com.mall.entity.Order;
import com.mall.entity.Payment;
import com.mall.mapper.OrderMapper;
import com.mall.mapper.PaymentMapper;
import com.mall.service.OrderService;
import com.mall.service.PaymentService;
import com.mall.vo.PaymentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    private static final int STATUS_PENDING = 0;
    private static final int STATUS_SUCCESS = 1;
    private static final int STATUS_FAILED = 2;
    private static final int STATUS_REFUNDED = 3;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Override
    @Transactional
    public PaymentVO createPayment(Long userId, PaymentCreateDTO dto) {
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getOrderNo, dto.getOrderNo());
        orderWrapper.eq(Order::getUserId, userId);
        Order order = orderMapper.selectOne(orderWrapper);

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getStatus().equals(0)) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }

        LambdaQueryWrapper<Payment> paymentWrapper = new LambdaQueryWrapper<>();
        paymentWrapper.eq(Payment::getOrderNo, dto.getOrderNo());
        paymentWrapper.eq(Payment::getStatus, STATUS_PENDING);
        Payment existingPayment = getOne(paymentWrapper);

        if (existingPayment != null) {
            return convertToVO(existingPayment);
        }

        Payment payment = new Payment();
        payment.setPaymentNo(generatePaymentNo());
        payment.setOrderNo(dto.getOrderNo());
        payment.setUserId(userId);
        payment.setPaymentType(dto.getPaymentType());
        payment.setAmount(dto.getAmount());
        payment.setStatus(STATUS_PENDING);
        save(payment);

        return convertToVO(payment);
    }

    @Override
    public String getPaymentUrl(Long userId, String paymentNo) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getPaymentNo, paymentNo);
        wrapper.eq(Payment::getUserId, userId);
        Payment payment = getOne(wrapper);

        if (payment == null) {
            throw new RuntimeException("支付记录不存在");
        }

        if (!payment.getStatus().equals(STATUS_PENDING)) {
            throw new RuntimeException("支付状态不正确");
        }

        if ("ALIPAY".equals(payment.getPaymentType())) {
            return "https://open.alipay.com/gateway.do?method=" + payment.getPaymentNo();
        } else if ("WECHAT".equals(payment.getPaymentType())) {
            return "weixin://wap/pay?prepayid=" + payment.getPaymentNo();
        } else {
            throw new RuntimeException("不支持的支付方式");
        }
    }

    @Override
    public PaymentVO getPaymentByNo(Long userId, String paymentNo) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getPaymentNo, paymentNo);
        wrapper.eq(Payment::getUserId, userId);
        Payment payment = getOne(wrapper);

        if (payment == null) {
            throw new RuntimeException("支付记录不存在");
        }

        return convertToVO(payment);
    }

    @Override
    public PaymentVO getPaymentByOrderNo(Long userId, String orderNo) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getOrderNo, orderNo);
        wrapper.eq(Payment::getUserId, userId);
        Payment payment = getOne(wrapper);

        if (payment == null) {
            throw new RuntimeException("支付记录不存在");
        }

        return convertToVO(payment);
    }

    @Override
    @Transactional
    public boolean handlePaymentNotify(String paymentType, Map<String, String> params) {
        String outTradeNo = params.get("out_trade_no");
        String transactionId = params.get("transaction_id");

        if (outTradeNo == null) {
            return false;
        }

        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getPaymentNo, outTradeNo);
        Payment payment = getOne(wrapper);

        if (payment == null) {
            return false;
        }

        if (payment.getStatus().equals(STATUS_SUCCESS)) {
            return true;
        }

        payment.setStatus(STATUS_SUCCESS);
        payment.setTransactionId(transactionId);
        payment.setPayTime(new Date());
        payment.setNotifyTime(new Date());
        updateById(payment);

        try {
            orderService.payOrder(payment.getUserId(), payment.getOrderNo());
            return true;
        } catch (Exception e) {
            payment.setStatus(STATUS_FAILED);
            updateById(payment);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean refundPayment(Long userId, String paymentNo, String refundReason) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getPaymentNo, paymentNo);
        wrapper.eq(Payment::getUserId, userId);
        Payment payment = getOne(wrapper);

        if (payment == null) {
            throw new RuntimeException("支付记录不存在");
        }

        if (!payment.getStatus().equals(STATUS_SUCCESS)) {
            throw new RuntimeException("支付状态不正确，无法退款");
        }

        payment.setStatus(STATUS_REFUNDED);
        payment.setRemark(refundReason);
        updateById(payment);

        return true;
    }

    private PaymentVO convertToVO(Payment payment) {
        PaymentVO vo = new PaymentVO();
        BeanUtils.copyProperties(payment, vo);
        vo.setStatusName(getStatusName(payment.getStatus()));
        vo.setPaymentTypeName(getPaymentTypeName(payment.getPaymentType()));
        return vo;
    }

    private String getStatusName(Integer status) {
        switch (status) {
            case STATUS_PENDING:
                return "待支付";
            case STATUS_SUCCESS:
                return "支付成功";
            case STATUS_FAILED:
                return "支付失败";
            case STATUS_REFUNDED:
                return "已退款";
            default:
                return "未知";
        }
    }

    private String getPaymentTypeName(String paymentType) {
        if ("ALIPAY".equals(paymentType)) {
            return "支付宝";
        } else if ("WECHAT".equals(paymentType)) {
            return "微信支付";
        } else {
            return "未知";
        }
    }

    private String generatePaymentNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return "PAY" + sdf.format(new Date());
    }
}
