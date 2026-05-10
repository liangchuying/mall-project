package com.mall.service;

import com.mall.dto.ProductBrandCreateDTO;
import com.mall.dto.ProductBrandUpdateDTO;
import com.mall.vo.ProductBrandVO;

import java.util.List;

public interface ProductBrandService {

    Long createBrand(ProductBrandCreateDTO dto);

    void updateBrand(ProductBrandUpdateDTO dto);

    void deleteBrand(Long id);

    ProductBrandVO getBrandById(Long id);

    List<ProductBrandVO> getAllBrands();
}
