package com.mall.controller;

import com.mall.dto.ProductSkuCreateDTO;
import com.mall.dto.ProductSkuUpdateDTO;
import com.mall.service.ProductSkuService;
import com.mall.utils.Result;
import com.mall.vo.ProductSkuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品SKU管理", description = "商品SKU相关接口")
@RestController
@RequestMapping("/product/sku")
public class ProductSkuController {

    @Autowired
    private ProductSkuService productSkuService;

    @Operation(summary = "创建SKU", description = "创建新的商品SKU")
    @PostMapping
    public Result<Long> createSku(@Valid @RequestBody ProductSkuCreateDTO dto) {
        Long id = productSkuService.createSku(dto);
        return Result.success(id);
    }

    @Operation(summary = "更新SKU", description = "更新商品SKU信息")
    @PutMapping
    public Result<Void> updateSku(@Valid @RequestBody ProductSkuUpdateDTO dto) {
        productSkuService.updateSku(dto);
        return Result.success();
    }

    @Operation(summary = "删除SKU", description = "根据ID删除商品SKU")
    @DeleteMapping("/{id}")
    public Result<Void> deleteSku(
            @Parameter(description = "SKU ID", required = true) @PathVariable Long id
    ) {
        productSkuService.deleteSku(id);
        return Result.success();
    }

    @Operation(summary = "获取SKU详情", description = "根据ID获取商品SKU详情")
    @GetMapping("/{id}")
    public Result<ProductSkuVO> getSkuById(
            @Parameter(description = "SKU ID", required = true) @PathVariable Long id
    ) {
        ProductSkuVO skuVO = productSkuService.getSkuById(id);
        return Result.success(skuVO);
    }

    @Operation(summary = "获取SPU的SKU列表", description = "根据SPU ID获取该商品的所有SKU")
    @GetMapping("/list/{spuId}")
    public Result<List<ProductSkuVO>> getSkuBySpuId(
            @Parameter(description = "SPU ID", required = true) @PathVariable Long spuId
    ) {
        List<ProductSkuVO> skuList = productSkuService.getSkuBySpuId(spuId);
        return Result.success(skuList);
    }

    @Operation(summary = "更新SKU状态", description = "上架或下架SKU")
    @PutMapping("/{id}/status")
    public Result<Void> updateSkuStatus(
            @Parameter(description = "SKU ID", required = true) @PathVariable Long id,
            @Parameter(description = "SKU状态：0=下架，1=上架", required = true) @RequestParam Integer status
    ) {
        productSkuService.updateSkuStatus(id, status);
        return Result.success();
    }
}
