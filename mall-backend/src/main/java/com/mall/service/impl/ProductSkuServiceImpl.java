package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dto.ProductSkuCreateDTO;
import com.mall.dto.ProductSkuUpdateDTO;
import com.mall.entity.ProductSku;
import com.mall.mapper.ProductSkuMapper;
import com.mall.service.ProductSkuService;
import com.mall.vo.ProductSkuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku> implements ProductSkuService {

    @Override
    @Transactional
    public Long createSku(ProductSkuCreateDTO dto) {
        ProductSku sku = new ProductSku();
        sku.setSpuId(dto.getSpuId());
        sku.setSkuCode(dto.getSkuCode());
        sku.setSpecs(dto.getSpecs());
        sku.setPrice(dto.getPrice());
        sku.setStock(dto.getStock());
        sku.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        save(sku);
        return sku.getId();
    }

    @Override
    @Transactional
    public void updateSku(ProductSkuUpdateDTO dto) {
        ProductSku sku = getById(dto.getId());
        if (sku == null) {
            throw new RuntimeException("SKU不存在");
        }

        if (dto.getSkuCode() != null) {
            sku.setSkuCode(dto.getSkuCode());
        }
        if (dto.getSpecs() != null) {
            sku.setSpecs(dto.getSpecs());
        }
        if (dto.getPrice() != null) {
            sku.setPrice(dto.getPrice());
        }
        if (dto.getStock() != null) {
            sku.setStock(dto.getStock());
        }
        if (dto.getStatus() != null) {
            sku.setStatus(dto.getStatus());
        }

        updateById(sku);
    }

    @Override
    @Transactional
    public void deleteSku(Long id) {
        ProductSku sku = getById(id);
        if (sku == null) {
            throw new RuntimeException("SKU不存在");
        }
        removeById(id);
    }

    @Override
    public ProductSkuVO getSkuById(Long id) {
        ProductSku sku = getById(id);
        if (sku == null) {
            throw new RuntimeException("SKU不存在");
        }
        return convertToVO(sku);
    }

    @Override
    public List<ProductSkuVO> getSkuBySpuId(Long spuId) {
        LambdaQueryWrapper<ProductSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSku::getSpuId, spuId);
        wrapper.orderByAsc(ProductSku::getCreateTime);
        List<ProductSku> skuList = list(wrapper);
        return skuList.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateSkuStatus(Long id, Integer status) {
        ProductSku sku = getById(id);
        if (sku == null) {
            throw new RuntimeException("SKU不存在");
        }
        sku.setStatus(status);
        updateById(sku);
    }

    private ProductSkuVO convertToVO(ProductSku sku) {
        ProductSkuVO vo = new ProductSkuVO();
        BeanUtils.copyProperties(sku, vo);
        return vo;
    }
}
