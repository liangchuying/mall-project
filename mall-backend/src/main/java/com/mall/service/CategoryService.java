package com.mall.service;

import com.mall.dto.CategoryCreateDTO;
import com.mall.dto.CategoryUpdateDTO;
import com.mall.entity.Category;
import com.mall.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    Long createCategory(CategoryCreateDTO dto);

    void updateCategory(CategoryUpdateDTO dto);

    void deleteCategory(Long id);

    CategoryVO getCategoryById(Long id);

    List<CategoryVO> getCategoryTree();
}
