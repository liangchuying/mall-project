package com.mall.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class StockLockDTO {
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    @NotNull(message = "SKU列表不能为空")
    private List<SkuStockItem> skuList;

    public static class SkuStockItem {
        @NotNull(message = "SKU ID不能为空")
        private Long skuId;

        @NotNull(message = "数量不能为空")
        private Integer quantity;

        public Long getSkuId() {
            return skuId;
        }

        public void setSkuId(Long skuId) {
            this.skuId = skuId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<SkuStockItem> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuStockItem> skuList) {
        this.skuList = skuList;
    }
}
