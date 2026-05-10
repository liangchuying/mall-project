package com.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.service.ProductSearchService;
import com.mall.utils.Result;
import com.mall.vo.ProductSpuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品检索", description = "商品检索相关接口")
@RestController
@RequestMapping("/product/search")
public class ProductSearchController {

    @Autowired
    private ProductSearchService productSearchService;

    @Operation(summary = "搜索商品", description = "根据关键词、分类、品牌等条件搜索商品")
    @GetMapping
    public Result<Page<ProductSpuVO>> searchProducts(
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "品牌ID") @RequestParam(required = false) Long brandId,
            @Parameter(description = "商品状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "排序字段：price=价格，sales=销量，createTime=创建时间，sort=排序号") @RequestParam(required = false) String sortField,
            @Parameter(description = "排序方向：asc=升序，desc=降序") @RequestParam(required = false) String sortOrder,
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<ProductSpuVO> page = new Page<>(current, size);
        Page<ProductSpuVO> result = productSearchService.searchProducts(page, keyword, categoryId, brandId, status, sortField, sortOrder);
        return Result.success(result);
    }

    @Operation(summary = "获取搜索建议", description = "根据关键词获取搜索建议")
    @GetMapping("/suggestions")
    public Result<List<String>> getSearchSuggestions(
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword
    ) {
        List<String> suggestions = productSearchService.getSearchSuggestions(keyword);
        return Result.success(suggestions);
    }
}
