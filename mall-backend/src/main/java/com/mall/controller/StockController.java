package com.mall.controller;

import com.mall.dto.*;
import com.mall.service.StockService;
import com.mall.utils.Result;
import com.mall.vo.StockRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "库存管理", description = "库存相关接口")
@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @Operation(summary = "入库", description = "增加商品库存")
    @PostMapping("/in")
    public Result<Void> stockIn(@Valid @RequestBody StockInDTO dto) {
        stockService.stockIn(dto);
        return Result.success();
    }

    @Operation(summary = "出库", description = "减少商品库存")
    @PostMapping("/out")
    public Result<Void> stockOut(@Valid @RequestBody StockOutDTO dto) {
        stockService.stockOut(dto);
        return Result.success();
    }

    @Operation(summary = "锁定库存", description = "订单创建时锁定库存")
    @PostMapping("/lock")
    public Result<Void> lockStock(@Valid @RequestBody StockLockDTO dto) {
        stockService.lockStock(dto);
        return Result.success();
    }

    @Operation(summary = "解锁库存", description = "订单取消时解锁库存")
    @PostMapping("/unlock/{orderNo}")
    public Result<Void> unlockStock(
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo
    ) {
        stockService.unlockStock(orderNo);
        return Result.success();
    }

    @Operation(summary = "扣减库存", description = "订单支付后扣减库存")
    @PostMapping("/deduct/{orderNo}")
    public Result<Void> deductStock(
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo
    ) {
        stockService.deductStock(orderNo);
        return Result.success();
    }

    @Operation(summary = "获取库存", description = "获取商品当前库存")
    @GetMapping("/{skuId}")
    public Result<Integer> getStock(
            @Parameter(description = "SKU ID", required = true) @PathVariable Long skuId
    ) {
        Integer stock = stockService.getStock(skuId);
        return Result.success(stock);
    }

    @Operation(summary = "获取库存变动记录", description = "查询库存变动记录")
    @GetMapping("/records")
    public Result<List<StockRecordVO>> getStockRecords(
            @Parameter(description = "SKU ID") @RequestParam(required = false) Long skuId,
            @Parameter(description = "变动类型：1=入库，2=出库，3=锁定，4=解锁，5=扣减") @RequestParam(required = false) Integer type,
            @Parameter(description = "订单号") @RequestParam(required = false) String orderNo
    ) {
        List<StockRecordVO> records = stockService.getStockRecords(skuId, type, orderNo);
        return Result.success(records);
    }
}
