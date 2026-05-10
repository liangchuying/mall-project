package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dto.ProductBrandCreateDTO;
import com.mall.dto.ProductBrandUpdateDTO;
import com.mall.entity.ProductBrand;
import com.mall.mapper.ProductBrandMapper;
import com.mall.service.ProductBrandService;
import com.mall.vo.ProductBrandVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductBrandServiceImpl extends ServiceImpl<ProductBrandMapper, ProductBrand> implements ProductBrandService {

    @Override
    @Transactional
    public Long createBrand(ProductBrandCreateDTO dto) {
        ProductBrand brand = new ProductBrand();
        brand.setName(dto.getName());
        brand.setLogo(dto.getLogo());
        brand.setDescription(dto.getDescription());
        brand.setSort(dto.getSort() != null ? dto.getSort() : 0);
        brand.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        save(brand);
        return brand.getId();
    }

    @Override
    @Transactional
    public void updateBrand(ProductBrandUpdateDTO dto) {
        ProductBrand brand = getById(dto.getId());
        if (brand == null) {
            throw new RuntimeException("品牌不存在");
        }

        if (dto.getName() != null) {
            brand.setName(dto.getName());
        }
        if (dto.getLogo() != null) {
            brand.setLogo(dto.getLogo());
        }
        if (dto.getDescription() != null) {
            brand.setDescription(dto.getDescription());
        }
        if (dto.getSort() != null) {
            brand.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            brand.setStatus(dto.getStatus());
        }

        updateById(brand);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        ProductBrand brand = getById(id);
        if (brand == null) {
            throw new RuntimeException("品牌不存在");
        }
        removeById(id);
    }

    @Override
    public ProductBrandVO getBrandById(Long id) {
        ProductBrand brand = getById(id);
        if (brand == null) {
            throw new RuntimeException("品牌不存在");
        }
        return convertToVO(brand);
    }

    @Override
    public List<ProductBrandVO> getAllBrands() {
        LambdaQueryWrapper<ProductBrand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductBrand::getStatus, 1);
        wrapper.orderByAsc(ProductBrand::getSort);
        List<ProductBrand> brandList = list(wrapper);
        return brandList.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private ProductBrandVO convertToVO(ProductBrand brand) {
        ProductBrandVO vo = new ProductBrandVO();
        BeanUtils.copyProperties(brand, vo);
        return vo;
    }
}
