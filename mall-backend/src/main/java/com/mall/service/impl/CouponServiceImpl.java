package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Coupon;
import com.mall.entity.UserCoupon;
import com.mall.mapper.CouponMapper;
import com.mall.mapper.UserCouponMapper;
import com.mall.service.CouponService;
import com.mall.vo.CouponVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    private static final int TYPE_FULL_REDUCTION = 1;
    private static final int TYPE_DISCOUNT = 2;
    private static final int TYPE_IMMEDIATE_REDUCTION = 3;

    private static final int STATUS_OFF = 0;
    private static final int STATUS_ONGOING = 1;
    private static final int STATUS_ENDED = 2;

    private static final int USER_COUPON_UNUSED = 0;
    private static final int USER_COUPON_USED = 1;
    private static final int USER_COUPON_EXPIRED = 2;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    @Transactional
    public Long createCoupon(String name, Integer type, BigDecimal discountAmount, BigDecimal discountRate, BigDecimal minAmount, Integer totalCount, Integer validDays) {
        Coupon coupon = new Coupon();
        coupon.setName(name);
        coupon.setType(type);
        coupon.setDiscountAmount(discountAmount);
        coupon.setDiscountRate(discountRate);
        coupon.setMinAmount(minAmount);
        coupon.setTotalCount(totalCount);
        coupon.setUsedCount(0);
        coupon.setReceiveCount(0);
        coupon.setStatus(STATUS_ONGOING);
        coupon.setValidDays(validDays);

        if (validDays != null && validDays > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, validDays);
            coupon.setValidEndTime(cal.getTime());
        }

        save(coupon);
        return coupon.getId();
    }

    @Override
    public List<CouponVO> getAvailableCoupons(Long userId) {
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupon::getStatus, STATUS_ONGOING);
        wrapper.orderByDesc(Coupon::getCreateTime);
        List<Coupon> couponList = list(wrapper);

        couponList = couponList.stream()
                .filter(c -> c.getTotalCount() > c.getReceiveCount())
                .collect(Collectors.toList());

        LambdaQueryWrapper<UserCoupon> userCouponWrapper = new LambdaQueryWrapper<>();
        userCouponWrapper.eq(UserCoupon::getUserId, userId);
        List<UserCoupon> userCouponList = userCouponMapper.selectList(userCouponWrapper);
        Set<Long> receivedCouponIds = userCouponList.stream().map(UserCoupon::getCouponId).collect(Collectors.toSet());

        Date now = new Date();

        return couponList.stream().map(coupon -> {
            CouponVO vo = new CouponVO();
            BeanUtils.copyProperties(coupon, vo);
            vo.setTypeName(getTypeName(coupon.getType()));
            vo.setStatusName(getStatusName(coupon.getStatus()));
            vo.setCanReceive(!receivedCouponIds.contains(coupon.getId()) && coupon.getReceiveCount() < coupon.getTotalCount());
            vo.setIsReceived(receivedCouponIds.contains(coupon.getId()));

            if (coupon.getValidEndTime() != null && coupon.getValidEndTime().before(now)) {
                vo.setCanReceive(false);
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String receiveCoupon(Long userId, Long couponId) {
        Coupon coupon = getById(couponId);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        if (!coupon.getStatus().equals(STATUS_ONGOING)) {
            throw new RuntimeException("优惠券不可领取");
        }
        if (coupon.getReceiveCount() >= coupon.getTotalCount()) {
            throw new RuntimeException("优惠券已领取完毕");
        }

        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCoupon::getUserId, userId);
        wrapper.eq(UserCoupon::getCouponId, couponId);
        if (userCouponMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("您已领取过该优惠券");
        }

        String couponCode = generateCouponCode();

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setCouponCode(couponCode);
        userCoupon.setStatus(USER_COUPON_UNUSED);

        if (coupon.getValidDays() != null && coupon.getValidDays() > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, coupon.getValidDays());
            userCoupon.setValidTime(cal.getTime());
        } else if (coupon.getValidEndTime() != null) {
            userCoupon.setValidTime(coupon.getValidEndTime());
        }

        userCouponMapper.insert(userCoupon);

        coupon.setReceiveCount(coupon.getReceiveCount() + 1);
        updateById(coupon);

        return couponCode;
    }

    @Override
    @Transactional
    public BigDecimal calculateDiscount(Long userId, Long couponId, BigDecimal orderAmount, String orderNo) {
        if (couponId == null) {
            return BigDecimal.ZERO;
        }

        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCoupon::getUserId, userId);
        wrapper.eq(UserCoupon::getCouponId, couponId);
        wrapper.eq(UserCoupon::getStatus, USER_COUPON_UNUSED);
        UserCoupon userCoupon = userCouponMapper.selectOne(wrapper);

        if (userCoupon == null) {
            throw new RuntimeException("优惠券不存在或已使用");
        }

        Date now = new Date();
        if (userCoupon.getValidTime() != null && userCoupon.getValidTime().before(now)) {
            throw new RuntimeException("优惠券已过期");
        }

        Coupon coupon = getById(couponId);
        BigDecimal discount = BigDecimal.ZERO;

        switch (coupon.getType()) {
            case TYPE_FULL_REDUCTION:
                if (orderAmount.compareTo(coupon.getMinAmount()) >= 0) {
                    discount = coupon.getDiscountAmount();
                }
                break;
            case TYPE_DISCOUNT:
                if (orderAmount.compareTo(coupon.getMinAmount()) >= 0) {
                    discount = orderAmount.multiply(BigDecimal.ONE.subtract(coupon.getDiscountRate()));
                }
                break;
            case TYPE_IMMEDIATE_REDUCTION:
                discount = coupon.getDiscountAmount();
                break;
        }

        userCoupon.setStatus(USER_COUPON_USED);
        userCoupon.setUseTime(now);
        userCoupon.setOrderNo(orderNo);
        userCouponMapper.updateById(userCoupon);

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        updateById(coupon);

        return discount;
    }

    @Override
    public List<CouponVO> getUserCoupons(Long userId, Integer status) {
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCoupon::getUserId, userId);
        if (status != null) {
            wrapper.eq(UserCoupon::getStatus, status);
        }
        wrapper.orderByDesc(UserCoupon::getReceiveTime);
        List<UserCoupon> userCouponList = userCouponMapper.selectList(wrapper);

        if (userCouponList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> couponIds = userCouponList.stream().map(UserCoupon::getCouponId).distinct().collect(Collectors.toList());
        List<Coupon> couponList = listByIds(couponIds);
        Map<Long, Coupon> couponMap = couponList.stream().collect(Collectors.toMap(Coupon::getId, c -> c));

        Date now = new Date();

        return userCouponList.stream().map(userCoupon -> {
            Coupon coupon = couponMap.get(userCoupon.getCouponId());
            if (coupon == null) {
                return null;
            }

            CouponVO vo = new CouponVO();
            BeanUtils.copyProperties(coupon, vo);
            vo.setTypeName(getTypeName(coupon.getType()));
            vo.setStatusName(getStatusName(coupon.getStatus()));

            if (userCoupon.getValidTime() != null && userCoupon.getValidTime().before(now)) {
                if (userCoupon.getStatus().equals(USER_COUPON_UNUSED)) {
                    userCoupon.setStatus(USER_COUPON_EXPIRED);
                    userCouponMapper.updateById(userCoupon);
                    vo.setStatusName("已过期");
                }
            }

            return vo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String getTypeName(Integer type) {
        switch (type) {
            case TYPE_FULL_REDUCTION:
                return "满减券";
            case TYPE_DISCOUNT:
                return "折扣券";
            case TYPE_IMMEDIATE_REDUCTION:
                return "立减券";
            default:
                return "未知";
        }
    }

    private String getStatusName(Integer status) {
        switch (status) {
            case STATUS_OFF:
                return "已下架";
            case STATUS_ONGOING:
                return "进行中";
            case STATUS_ENDED:
                return "已结束";
            default:
                return "未知";
        }
    }

    private String generateCouponCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return "CPN" + sdf.format(new Date()) + String.format("%04d", new Random().nextInt(10000));
    }
}
