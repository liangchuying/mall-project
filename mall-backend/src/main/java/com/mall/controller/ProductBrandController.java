package com.mall.controller;

import com.mall.dto.ProductBrandCreateDTO;
import com.mall.dto.ProductBrandUpdateDTO;
import com.mall.service.ProductBrandService;
import com.mall.utils.Result;
import com.mall.vo.ProductBrandVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品品牌管理", description = "商品品牌相关接口")
@RestController
@RequestMapping("/product/brand")
public class ProductBrandController {

    @Autowired
    private ProductBrandService productBrandService;

    @Operation(summary = "创建品牌", description = "创建新的商品品牌")
    @PostMapping
    public Result<Long> createBrand(@Valid @RequestBody ProductBrandCreateDTO dto) {
        Long id = productBrandService.createBrand(dto);
        return Result.success(id);
    }

    @Operation(summary = "更新品牌", description = "更新商品品牌信息")
    @PutMapping
    public Result<Void> updateBrand(@Valid @RequestBody ProductBrandUpdateDTO dto) {
        productBrandService.updateBrand(dto);
        return Result.success();
    }

    @Operation(summary = "删除品牌", description = "根据ID删除商品品牌")
    @DeleteMapping("/{id}")
    public Result<Void> deleteBrand(
            @Parameter(description = "品牌ID", required = true) @PathVariable Long id
    ) {
        productBrandService.deleteBrand(id);
        return Result.success();
    }

    @Operation(summary = "获取品牌详情", description = "根据ID获取商品品牌详情")
    @GetMapping("/{id}")
    public Result<ProductBrandVO> getBrandById(
            @Parameter(description = "品牌ID", required = true) @PathVariable Long id
    ) {
        ProductBrandVO brandVO = productBrandService.getBrandById(id);
        return Result.success(brandVO);
    }

    @Operation(summary = "获取所有品牌", description = "获取所有启用的商品品牌列表")
    @GetMapping("/list")
    public Result<List<ProductBrandVO>> getAllBrands() {
        List<ProductBrandVO> brandList = productBrandService.getAllBrands();
        return Result.success(brandList);
    }
}
