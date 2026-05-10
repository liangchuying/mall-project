package com.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.dto.ProductSpuCreateDTO;
import com.mall.dto.ProductSpuUpdateDTO;
import com.mall.vo.ProductSpuVO;

import java.util.List;

public interface ProductSpuService {

    Long createProduct(ProductSpuCreateDTO dto);

    void updateProduct(ProductSpuUpdateDTO dto);

    void deleteProduct(Long id);

    ProductSpuVO getProductById(Long id);

    Page<ProductSpuVO> getProductPage(Page<ProductSpuVO> page, Long categoryId, Long brandId, String name, Integer status);

    void updateProductStatus(Long id, Integer status);
}
