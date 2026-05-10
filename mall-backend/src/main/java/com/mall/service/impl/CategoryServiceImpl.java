package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dto.CategoryCreateDTO;
import com.mall.dto.CategoryUpdateDTO;
import com.mall.entity.Category;
import com.mall.mapper.CategoryMapper;
import com.mall.service.CategoryService;
import com.mall.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    @Transactional
    public Long createCategory(CategoryCreateDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setParentId(dto.getParentId());
        category.setLevel(dto.getParentId() == 0 ? 1 : getParentLevel(dto.getParentId()) + 1);
        category.setIcon(dto.getIcon());
        category.setSort(dto.getSort() != null ? dto.getSort() : 0);
        category.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        save(category);
        return category.getId();
    }

    @Override
    @Transactional
    public void updateCategory(CategoryUpdateDTO dto) {
        Category category = getById(dto.getId());
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        if (dto.getIcon() != null) {
            category.setIcon(dto.getIcon());
        }
        if (dto.getSort() != null) {
            category.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            category.setStatus(dto.getStatus());
        }

        updateById(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, id);
        Long count = count(wrapper);
        if (count > 0) {
            throw new RuntimeException("该分类下有子分类，无法删除");
        }

        removeById(id);
    }

    @Override
    public CategoryVO getCategoryById(Long id) {
        Category category = getById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        return convertToVO(category);
    }

    @Override
    public List<CategoryVO> getCategoryTree() {
        List<Category> allCategories = list();
        List<CategoryVO> topCategories = allCategories.stream()
                .filter(c -> c.getParentId() == 0)
                .map(this::convertToVO)
                .sorted((a, b) -> a.getSort().compareTo(b.getSort()))
                .collect(Collectors.toList());

        for (CategoryVO topCategory : topCategories) {
            buildCategoryTree(topCategory, allCategories);
        }

        return topCategories;
    }

    private CategoryVO convertToVO(Category category) {
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }

    private void buildCategoryTree(CategoryVO parentVO, List<Category> allCategories) {
        List<CategoryVO> children = allCategories.stream()
                .filter(c -> c.getParentId().equals(parentVO.getId()))
                .map(this::convertToVO)
                .sorted((a, b) -> a.getSort().compareTo(b.getSort()))
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
            parentVO.setChildren(children);
            for (CategoryVO child : children) {
                buildCategoryTree(child, allCategories);
            }
        }
    }

    private Integer getParentLevel(Long parentId) {
        if (parentId == 0) {
            return 0;
        }
        Category parent = getById(parentId);
        return parent.getLevel();
    }
}
