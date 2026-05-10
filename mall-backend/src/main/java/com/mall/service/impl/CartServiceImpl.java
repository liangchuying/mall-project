package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dto.CartAddDTO;
import com.mall.dto.CartUpdateDTO;
import com.mall.entity.Cart;
import com.mall.entity.ProductImage;
import com.mall.entity.ProductSku;
import com.mall.entity.ProductSpu;
import com.mall.mapper.CartMapper;
import com.mall.mapper.ProductImageMapper;
import com.mall.mapper.ProductSkuMapper;
import com.mall.mapper.ProductSpuMapper;
import com.mall.service.CartService;
import com.mall.vo.CartVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductSpuMapper productSpuMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Override
    @Transactional
    public Long addToCart(Long userId, CartAddDTO dto) {
        ProductSku sku = productSkuMapper.selectById(dto.getSkuId());
        if (sku == null) {
            throw new RuntimeException("SKU不存在");
        }
        if (sku.getStock() < dto.getQuantity()) {
            throw new RuntimeException("库存不足");
        }

        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        wrapper.eq(Cart::getSkuId, dto.getSkuId());
        Cart existingCart = getOne(wrapper);

        if (existingCart != null) {
            existingCart.setQuantity(existingCart.getQuantity() + dto.getQuantity());
            updateById(existingCart);
            return existingCart.getId();
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setSkuId(dto.getSkuId());
            cart.setQuantity(dto.getQuantity());
            cart.setSelected(1);
            save(cart);
            return cart.getId();
        }
    }

    @Override
    @Transactional
    public void updateCart(Long userId, CartUpdateDTO dto) {
        Cart cart = getById(dto.getId());
        if (cart == null) {
            throw new RuntimeException("购物车项不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此购物车项");
        }

        if (dto.getQuantity() != null) {
            if (dto.getQuantity() <= 0) {
                throw new RuntimeException("数量必须大于0");
            }
            ProductSku sku = productSkuMapper.selectById(cart.getSkuId());
            if (sku != null && sku.getStock() < dto.getQuantity()) {
                throw new RuntimeException("库存不足");
            }
            cart.setQuantity(dto.getQuantity());
        }
        if (dto.getSelected() != null) {
            cart.setSelected(dto.getSelected());
        }

        updateById(cart);
    }

    @Override
    @Transactional
    public void deleteCart(Long userId, Long cartId) {
        Cart cart = getById(cartId);
        if (cart == null) {
            throw new RuntimeException("购物车项不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此购物车项");
        }
        removeById(cartId);
    }

    @Override
    @Transactional
    public void deleteCartBatch(Long userId, List<Long> cartIds) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        wrapper.in(Cart::getId, cartIds);
        remove(wrapper);
    }

    @Override
    public List<CartVO> getCartList(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        wrapper.orderByDesc(Cart::getUpdateTime);
        List<Cart> cartList = list(wrapper);

        if (cartList.isEmpty()) {
            return new ArrayList<>();
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

        List<CartVO> voList = new ArrayList<>();
        for (Cart cart : cartList) {
            ProductSku sku = skuMap.get(cart.getSkuId());
            if (sku == null) {
                continue;
            }

            ProductSpu spu = spuMap.get(sku.getSpuId());
            ProductImage image = imageMap.get(sku.getSpuId());

            CartVO vo = new CartVO();
            vo.setId(cart.getId());
            vo.setUserId(cart.getUserId());
            vo.setSkuId(cart.getSkuId());
            vo.setSkuCode(sku.getSkuCode());
            vo.setSpecs(sku.getSpecs());
            vo.setPrice(sku.getPrice());
            vo.setQuantity(cart.getQuantity());
            vo.setSelected(cart.getSelected());

            if (spu != null) {
                vo.setProductName(spu.getName());
            }
            if (image != null) {
                vo.setImageUrl(image.getImageUrl());
            }

            BigDecimal totalPrice = sku.getPrice().multiply(new BigDecimal(cart.getQuantity()));
            vo.setTotalPrice(totalPrice);

            voList.add(vo);
        }

        return voList;
    }

    @Override
    @Transactional
    public void selectAll(Long userId, Boolean selected) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        List<Cart> cartList = list(wrapper);

        for (Cart cart : cartList) {
            cart.setSelected(selected ? 1 : 0);
            updateById(cart);
        }
    }

    @Override
    public BigDecimal getCartTotalPrice(Long userId) {
        List<CartVO> cartList = getCartList(userId);
        BigDecimal total = BigDecimal.ZERO;

        for (CartVO vo : cartList) {
            if (vo.getSelected() != null && vo.getSelected() == 1) {
                total = total.add(vo.getTotalPrice());
            }
        }

        return total;
    }

    @Override
    public Integer getCartCount(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        wrapper.eq(Cart::getSelected, 1);
        List<Cart> cartList = list(wrapper);

        int totalCount = 0;
        for (Cart cart : cartList) {
            totalCount += cart.getQuantity();
        }

        return totalCount;
    }
}
