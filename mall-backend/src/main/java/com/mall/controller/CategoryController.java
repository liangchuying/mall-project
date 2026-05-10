package com.mall.controller;

import com.mall.dto.CategoryCreateDTO;
import com.mall.dto.CategoryUpdateDTO;
import com.mall.service.CategoryService;
import com.mall.utils.Result;
import com.mall.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品分类管理", description = "商品分类相关接口")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "创建商品分类", description = "创建新的商品分类")
    @PostMapping
    public Result<Long> createCategory(@Valid @RequestBody CategoryCreateDTO dto) {
        Long id = categoryService.createCategory(dto);
        return Result.success(id);
    }

    @Operation(summary = "更新商品分类", description = "更新商品分类信息")
    @PutMapping
    public Result<Void> updateCategory(@Valid @RequestBody CategoryUpdateDTO dto) {
        categoryService.updateCategory(dto);
        return Result.success();
    }

    @Operation(summary = "删除商品分类", description = "根据ID删除商品分类")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    @Operation(summary = "获取分类详情", description = "根据ID获取商品分类详情")
    @GetMapping("/{id}")
    public Result<CategoryVO> getCategoryById(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id
    ) {
        CategoryVO categoryVO = categoryService.getCategoryById(id);
        return Result.success(categoryVO);
    }

    @Operation(summary = "获取分类树", description = "获取所有分类的树形结构")
    @GetMapping("/tree")
    public Result<List<CategoryVO>> getCategoryTree() {
        List<CategoryVO> categoryTree = categoryService.getCategoryTree();
        return Result.success(categoryTree);
    }
}
