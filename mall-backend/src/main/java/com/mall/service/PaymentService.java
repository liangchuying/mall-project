package com.mall.service;

import com.mall.dto.PaymentCreateDTO;
import com.mall.vo.PaymentVO;

import java.util.Map;

public interface PaymentService {

    PaymentVO createPayment(Long userId, PaymentCreateDTO dto);

    String getPaymentUrl(Long userId, String paymentNo);

    PaymentVO getPaymentByNo(Long userId, String paymentNo);

    PaymentVO getPaymentByOrderNo(Long userId, String orderNo);

    boolean handlePaymentNotify(String paymentType, Map<String, String> params);

    boolean refundPayment(Long userId, String paymentNo, String refundReason);
}
