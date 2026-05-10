package com.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.vo.ProductSpuVO;

import java.util.List;

public interface ProductSearchService {

    Page<ProductSpuVO> searchProducts(Page<ProductSpuVO> page, String keyword, Long categoryId, Long brandId, Integer status, String sortField, String sortOrder);

    List<String> getSearchSuggestions(String keyword);
}
