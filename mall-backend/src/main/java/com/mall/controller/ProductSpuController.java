package com.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.dto.ProductSpuCreateDTO;
import com.mall.dto.ProductSpuUpdateDTO;
import com.mall.service.ProductSpuService;
import com.mall.utils.Result;
import com.mall.vo.ProductSpuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品SPU管理", description = "商品SPU相关接口")
@RestController
@RequestMapping("/product/spu")
public class ProductSpuController {

    @Autowired
    private ProductSpuService productSpuService;

    @Operation(summary = "创建商品", description = "创建新商品（SPU）及关联的SKU和图片")
    @PostMapping
    public Result<Long> createProduct(@Valid @RequestBody ProductSpuCreateDTO dto) {
        Long id = productSpuService.createProduct(dto);
        return Result.success(id);
    }

    @Operation(summary = "更新商品", description = "更新商品信息（SPU）")
    @PutMapping
    public Result<Void> updateProduct(@Valid @RequestBody ProductSpuUpdateDTO dto) {
        productSpuService.updateProduct(dto);
        return Result.success();
    }

    @Operation(summary = "删除商品", description = "根据ID删除商品（SPU）及其关联的SKU和图片")
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(
            @Parameter(description = "商品SPU ID", required = true) @PathVariable Long id
    ) {
        productSpuService.deleteProduct(id);
        return Result.success();
    }

    @Operation(summary = "获取商品详情", description = "根据ID获取商品详情，包含SKU列表和图片列表")
    @GetMapping("/{id}")
    public Result<ProductSpuVO> getProductById(
            @Parameter(description = "商品SPU ID", required = true) @PathVariable Long id
    ) {
        ProductSpuVO productVO = productSpuService.getProductById(id);
        return Result.success(productVO);
    }

    @Operation(summary = "分页查询商品", description = "分页查询商品列表，支持按分类、品牌、名称、状态筛选")
    @GetMapping("/page")
    public Result<Page<ProductSpuVO>> getProductPage(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "品牌ID") @RequestParam(required = false) Long brandId,
            @Parameter(description = "商品名称") @RequestParam(required = false) String name,
            @Parameter(description = "商品状态") @RequestParam(required = false) Integer status
    ) {
        Page<ProductSpuVO> page = new Page<>(current, size);
        Page<ProductSpuVO> result = productSpuService.getProductPage(page, categoryId, brandId, name, status);
        return Result.success(result);
    }

    @Operation(summary = "更新商品状态", description = "上架或下架商品")
    @PutMapping("/{id}/status")
    public Result<Void> updateProductStatus(
            @Parameter(description = "商品SPU ID", required = true) @PathVariable Long id,
            @Parameter(description = "商品状态：0=下架，1=上架", required = true) @RequestParam Integer status
    ) {
        productSpuService.updateProductStatus(id, status);
        return Result.success();
    }
}
