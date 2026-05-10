package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dto.OrderCreateDTO;
import com.mall.entity.*;
import com.mall.mapper.*;
import com.mall.service.OrderService;
import com.mall.service.StockService;
import com.mall.vo.OrderItemVO;
import com.mall.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private static final int STATUS_PENDING_PAYMENT = 0;
    private static final int STATUS_PAID = 1;
    private static final int STATUS_SHIPPED = 2;
    private static final int STATUS_RECEIVED = 3;
    private static final int STATUS_COMPLETED = 4;
    private static final int STATUS_CANCELLED = 5;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductSpuMapper productSpuMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private StockService stockService;

    @Override
    @Transactional
    public OrderVO createOrder(Long userId, OrderCreateDTO dto) {
        LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(Cart::getUserId, userId);
        cartWrapper.in(Cart::getId, dto.getCartIds());
        cartWrapper.eq(Cart::getSelected, 1);
        List<Cart> cartList = cartMapper.selectList(cartWrapper);

        if (cartList.isEmpty()) {
            throw new RuntimeException("购物车中没有选中的商品");
        }

        List<Long> skuIds = cartList.stream().map(Cart::getSkuId).collect(Collectors.toList());
        List<ProductSku> skuList = productSkuMapper.selectBatchIds(skuIds);
        Map<Long, ProductSku> skuMap = skuList.stream().collect(Collectors.toMap(ProductSku::getId, s -> s));

        List<Long> spuIds = skuList.stream().map(ProductSku::getSpuId).distinct().collect(Collectors.toList());
        List<ProductSpu> spuList = productSpuMapper.selectBatchIds(spuIds);
        Map<Long, ProductSpu> spuMap = spuList.stream().collect(Collectors.toMap(ProductSpu::getId, s -> s));

        List<ProductImage> imageList = productImageMapper.selectList(new LambdaQueryWrapper<ProductImage>()
                .in(ProductImage::getSpuId, spuIds)
                .eq(ProductImage::getIsMain, 1));
        Map<Long, ProductImage> imageMap = imageList.stream().collect(Collectors.toMap(ProductImage::getSpuId, i -> i));

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<com.mall.dto.StockLockDTO.SkuStockItem> stockItems = new ArrayList<>();

        for (Cart cart : cartList) {
            ProductSku sku = skuMap.get(cart.getSkuId());
            if (sku == null) {
                throw new RuntimeException("商品不存在");
            }
            if (sku.getStock() < cart.getQuantity()) {
                throw new RuntimeException("商品库存不足: " + sku.getSkuCode());
            }

            BigDecimal itemTotal = sku.getPrice().multiply(new BigDecimal(cart.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            com.mall.dto.StockLockDTO.SkuStockItem stockItem = new com.mall.dto.StockLockDTO.SkuStockItem();
            stockItem.setSkuId(sku.getId());
            stockItem.setQuantity(cart.getQuantity());
            stockItems.add(stockItem);
        }

        String orderNo = generateOrderNo();

        com.mall.dto.StockLockDTO lockDTO = new com.mall.dto.StockLockDTO();
        lockDTO.setOrderNo(orderNo);
        lockDTO.setSkuList(stockItems);
        stockService.lockStock(lockDTO);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setStatus(STATUS_PENDING_PAYMENT);
        order.setReceiverName(dto.getReceiverName());
        order.setReceiverPhone(dto.getReceiverPhone());
        order.setReceiverAddress(dto.getReceiverAddress());
        order.setRemark(dto.getRemark());
        save(order);

        for (Cart cart : cartList) {
            ProductSku sku = skuMap.get(cart.getSkuId());
            ProductSpu spu = spuMap.get(sku.getSpuId());
            ProductImage image = imageMap.get(sku.getSpuId());

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setOrderNo(orderNo);
            item.setSkuId(sku.getId());
            item.setSkuCode(sku.getSkuCode());
            item.setProductName(spu != null ? spu.getName() : "");
            item.setSpecs(sku.getSpecs());
            item.setImageUrl(image != null ? image.getImageUrl() : "");
            item.setPrice(sku.getPrice());
            item.setQuantity(cart.getQuantity());
            item.setTotalPrice(sku.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            orderItemMapper.insert(item);
        }

        cartMapper.deleteBatchIds(cartList);

        return getOrderDetail(userId, order.getId());
    }

    @Override
    @Transactional
    public OrderVO payOrder(Long userId, String orderNo) {
        Order order = getOrderByNoAndUserId(orderNo, userId);
        if (order.getStatus() != STATUS_PENDING_PAYMENT) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }

        order.setStatus(STATUS_PAID);
        order.setPayTime(new Date());
        updateById(order);

        stockService.deductStock(orderNo);

        return getOrderDetail(userId, order.getId());
    }

    @Override
    @Transactional
    public OrderVO cancelOrder(Long userId, String orderNo) {
        Order order = getOrderByNoAndUserId(orderNo, userId);
        if (order.getStatus() != STATUS_PENDING_PAYMENT) {
            throw new RuntimeException("订单状态不正确，无法取消");
        }

        order.setStatus(STATUS_CANCELLED);
        updateById(order);

        stockService.unlockStock(orderNo);

        return getOrderDetail(userId, order.getId());
    }

    @Override
    @Transactional
    public OrderVO shipOrder(String orderNo) {
        Order order = getOrderByNo(orderNo);
        if (order.getStatus() != STATUS_PAID) {
            throw new RuntimeException("订单状态不正确，无法发货");
        }

        order.setStatus(STATUS_SHIPPED);
        order.setDeliveryTime(new Date());
        updateById(order);

        return getOrderById(order.getId());
    }

    @Override
    @Transactional
    public OrderVO receiveOrder(Long userId, String orderNo) {
        Order order = getOrderByNoAndUserId(orderNo, userId);
        if (order.getStatus() != STATUS_SHIPPED) {
            throw new RuntimeException("订单状态不正确，无法收货");
        }

        order.setStatus(STATUS_RECEIVED);
        order.setReceiveTime(new Date());
        updateById(order);

        return getOrderDetail(userId, order.getId());
    }

    @Override
    @Transactional
    public OrderVO completeOrder(String orderNo) {
        Order order = getOrderByNo(orderNo);
        if (order.getStatus() != STATUS_RECEIVED) {
            throw new RuntimeException("订单状态不正确，无法完成");
        }

        order.setStatus(STATUS_COMPLETED);
        updateById(order);

        return getOrderById(order.getId());
    }

    @Override
    public OrderVO getOrderByNo(Long userId, String orderNo) {
        Order order = getOrderByNoAndUserId(orderNo, userId);
        return getOrderDetail(userId, order.getId());
    }

    @Override
    public Page<OrderVO> getUserOrders(Long userId, Integer status, Page<OrderVO> page) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> orderPage = new Page<>(page.getCurrent(), page.getSize());
        orderPage = page(orderPage, wrapper);
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());

        List<OrderVO> voList = orderPage.getRecords().stream().map(order -> {
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(order, vo);
            vo.setStatusName(getStatusName(order.getStatus()));
            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public OrderVO getOrderDetail(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此订单");
        }

        return buildOrderVO(order);
    }

    private Order getOrderByNo(String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = getOne(wrapper);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        return order;
    }

    private Order getOrderByNoAndUserId(String orderNo, Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        wrapper.eq(Order::getUserId, userId);
        Order order = getOne(wrapper);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        return order;
    }

    private OrderVO getOrderById(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        return buildOrderVO(order);
    }

    private OrderVO buildOrderVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setStatusName(getStatusName(order.getStatus()));

        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> items = orderItemMapper.selectList(itemWrapper);

        List<OrderItemVO> itemVOList = items.stream().map(this::convertOrderItemToVO).collect(Collectors.toList());
        vo.setItems(itemVOList);

        return vo;
    }

    private OrderItemVO convertOrderItemToVO(OrderItem item) {
        OrderItemVO vo = new OrderItemVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }

    private String getStatusName(Integer status) {
        switch (status) {
            case STATUS_PENDING_PAYMENT:
                return "待支付";
            case STATUS_PAID:
                return "已支付";
            case STATUS_SHIPPED:
                return "已发货";
            case STATUS_RECEIVED:
                return "已收货";
            case STATUS_COMPLETED:
                return "已完成";
            case STATUS_CANCELLED:
                return "已取消";
            default:
                return "未知";
        }
    }

    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return "ORD" + sdf.format(new Date());
    }
}
