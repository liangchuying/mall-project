package com.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.dto.OrderCreateDTO;
import com.mall.vo.OrderVO;

import java.util.List;

public interface OrderService {

    OrderVO createOrder(Long userId, OrderCreateDTO dto);

    OrderVO payOrder(Long userId, String orderNo);

    OrderVO cancelOrder(Long userId, String orderNo);

    OrderVO shipOrder(String orderNo);

    OrderVO receiveOrder(Long userId, String orderNo);

    OrderVO completeOrder(String orderNo);

    OrderVO getOrderByNo(Long userId, String orderNo);

    Page<OrderVO> getUserOrders(Long userId, Integer status, Page<OrderVO> page);

    OrderVO getOrderDetail(Long userId, Long orderId);
}
