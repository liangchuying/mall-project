package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dto.ProductSpuCreateDTO;
import com.mall.dto.ProductSpuUpdateDTO;
import com.mall.entity.*;
import com.mall.mapper.*;
import com.mall.service.ProductSpuService;
import com.mall.vo.ProductSpuVO;
import com.mall.vo.ProductImageVO;
import com.mall.vo.ProductSkuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductSpuServiceImpl extends ServiceImpl<ProductSpuMapper, ProductSpu> implements ProductSpuService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductBrandMapper productBrandMapper;

    @Override
    @Transactional
    public Long createProduct(ProductSpuCreateDTO dto) {
        ProductSpu spu = new ProductSpu();
        spu.setName(dto.getName());
        spu.setCategoryId(dto.getCategoryId());
        spu.setBrandId(dto.getBrandId() != null ? dto.getBrandId() : 0L);
        spu.setUnit(dto.getUnit() != null ? dto.getUnit() : "件");
        spu.setDescription(dto.getDescription());
        spu.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        spu.setSort(dto.getSort() != null ? dto.getSort() : 0);
        save(spu);

        if (dto.getSkuList() != null && !dto.getSkuList().isEmpty()) {
            for (ProductSpuCreateDTO.SkuDTO skuDTO : dto.getSkuList()) {
                ProductSku sku = new ProductSku();
                sku.setSpuId(spu.getId());
                sku.setSkuCode(skuDTO.getSkuCode());
                sku.setSpecs(skuDTO.getSpecs());
                sku.setPrice(skuDTO.getPrice());
                sku.setStock(skuDTO.getStock());
                sku.setStatus(skuDTO.getStatus() != null ? skuDTO.getStatus() : 1);
                productSkuMapper.insert(sku);
            }
        }

        if (dto.getImageList() != null && !dto.getImageList().isEmpty()) {
            for (ProductSpuCreateDTO.ImageDTO imageDTO : dto.getImageList()) {
                ProductImage image = new ProductImage();
                image.setSpuId(spu.getId());
                image.setImageUrl(imageDTO.getImageUrl());
                image.setIsMain(imageDTO.getIsMain() != null ? imageDTO.getIsMain() : 0);
                image.setSort(imageDTO.getSort() != null ? imageDTO.getSort() : 0);
                productImageMapper.insert(image);
            }
        }

        return spu.getId();
    }

    @Override
    @Transactional
    public void updateProduct(ProductSpuUpdateDTO dto) {
        ProductSpu spu = getById(dto.getId());
        if (spu == null) {
            throw new RuntimeException("商品不存在");
        }

        if (dto.getName() != null) {
            spu.setName(dto.getName());
        }
        if (dto.getCategoryId() != null) {
            spu.setCategoryId(dto.getCategoryId());
        }
        if (dto.getBrandId() != null) {
            spu.setBrandId(dto.getBrandId());
        }
        if (dto.getUnit() != null) {
            spu.setUnit(dto.getUnit());
        }
        if (dto.getDescription() != null) {
            spu.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            spu.setStatus(dto.getStatus());
        }
        if (dto.getSort() != null) {
            spu.setSort(dto.getSort());
        }

        updateById(spu);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        ProductSpu spu = getById(id);
        if (spu == null) {
            throw new RuntimeException("商品不存在");
        }

        LambdaQueryWrapper<ProductSku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ProductSku::getSpuId, id);
        productSkuMapper.delete(skuWrapper);

        LambdaQueryWrapper<ProductImage> imageWrapper = new LambdaQueryWrapper<>();
        imageWrapper.eq(ProductImage::getSpuId, id);
        productImageMapper.delete(imageWrapper);

        removeById(id);
    }

    @Override
    public ProductSpuVO getProductById(Long id) {
        ProductSpu spu = getById(id);
        if (spu == null) {
            throw new RuntimeException("商品不存在");
        }

        ProductSpuVO vo = new ProductSpuVO();
        BeanUtils.copyProperties(spu, vo);

        Category category = categoryMapper.selectById(spu.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }

        if (spu.getBrandId() != null && spu.getBrandId() > 0) {
            ProductBrand brand = productBrandMapper.selectById(spu.getBrandId());
            if (brand != null) {
                vo.setBrandName(brand.getName());
            }
        }

        LambdaQueryWrapper<ProductSku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ProductSku::getSpuId, id);
        List<ProductSku> skuList = productSkuMapper.selectList(skuWrapper);
        List<ProductSkuVO> skuVOList = skuList.stream().map(this::convertSkuToVO).collect(Collectors.toList());
        vo.setSkuList(skuVOList);

        LambdaQueryWrapper<ProductImage> imageWrapper = new LambdaQueryWrapper<>();
        imageWrapper.eq(ProductImage::getSpuId, id);
        imageWrapper.orderByAsc(ProductImage::getSort);
        List<ProductImage> imageList = productImageMapper.selectList(imageWrapper);
        List<ProductImageVO> imageVOList = imageList.stream().map(this::convertImageToVO).collect(Collectors.toList());
        vo.setImageList(imageVOList);

        return vo;
    }

    @Override
    public Page<ProductSpuVO> getProductPage(Page<ProductSpuVO> page, Long categoryId, Long brandId, String name, Integer status) {
        LambdaQueryWrapper<ProductSpu> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(ProductSpu::getCategoryId, categoryId);
        }
        if (brandId != null) {
            wrapper.eq(ProductSpu::getBrandId, brandId);
        }
        if (name != null && !name.isEmpty()) {
            wrapper.like(ProductSpu::getName, name);
        }
        if (status != null) {
            wrapper.eq(ProductSpu::getStatus, status);
        }
        wrapper.orderByDesc(ProductSpu::getCreateTime);

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
    @Transactional
    public void updateProductStatus(Long id, Integer status) {
        ProductSpu spu = getById(id);
        if (spu == null) {
            throw new RuntimeException("商品不存在");
        }
        spu.setStatus(status);
        updateById(spu);
    }

    private ProductSkuVO convertSkuToVO(ProductSku sku) {
        ProductSkuVO vo = new ProductSkuVO();
        BeanUtils.copyProperties(sku, vo);
        return vo;
    }

    private ProductImageVO convertImageToVO(ProductImage image) {
        ProductImageVO vo = new ProductImageVO();
        BeanUtils.copyProperties(image, vo);
        return vo;
    }
}
