package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.ProductSpu;
import com.mall.mapper.ProductSpuMapper;
import com.mall.service.ProductSearchService;
import com.mall.vo.ProductSpuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSearchServiceImpl extends ServiceImpl<ProductSpuMapper, ProductSpu> implements ProductSearchService {

    @Override
    public Page<ProductSpuVO> searchProducts(Page<ProductSpuVO> page, String keyword, Long categoryId, Long brandId, Integer status, String sortField, String sortOrder) {
        LambdaQueryWrapper<ProductSpu> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ProductSpu::getName, keyword);
        }
        if (categoryId != null) {
            wrapper.eq(ProductSpu::getCategoryId, categoryId);
        }
        if (brandId != null) {
            wrapper.eq(ProductSpu::getBrandId, brandId);
        }
        if (status != null) {
            wrapper.eq(ProductSpu::getStatus, status);
        }

        if (sortField != null && !sortField.isEmpty()) {
            if ("price".equals(sortField)) {
                // 按价格排序（需要关联 SKU 查询，这里简化处理按创建时间）
                wrapper.orderBy(true, "asc".equals(sortOrder), ProductSpu::getCreateTime);
            } else if ("sales".equals(sortField)) {
                // 按销量排序（暂时没有销量字段，按创建时间替代）
                wrapper.orderBy(true, "asc".equals(sortOrder), ProductSpu::getCreateTime);
            } else if ("createTime".equals(sortField)) {
                wrapper.orderBy(true, "asc".equals(sortOrder), ProductSpu::getCreateTime);
            } else if ("sort".equals(sortField)) {
                wrapper.orderBy(true, "asc".equals(sortOrder), ProductSpu::getSort);
            }
        } else {
            // 默认按创建时间倒序
            wrapper.orderByDesc(ProductSpu::getCreateTime);
        }

        Page<ProductSpu> spuPage = new Page<>(page.getCurrent(), page.getSize());
        spuPage = page(spuPage, wrapper);
        Page<ProductSpuVO> voPage = new Page<>(spuPage.getCurrent(), spuPage.getSize(), spuPage.getTotal());

        List<ProductSpuVO> voList = spuPage.getRecords().stream().map(spu -> {
            ProductSpuVO vo = new ProductSpuVO();
            BeanUtils.copyProperties(spu, vo);
            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<String> getSearchSuggestions(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<ProductSpu> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(ProductSpu::getName, keyword);
        wrapper.select(ProductSpu::getName);
        wrapper.last("LIMIT 10");

        List<ProductSpu> spuList = list(wrapper);
        return spuList.stream().map(ProductSpu::getName).collect(Collectors.toList());
    }
}
