package com.mall.dto;

import jakarta.validation.constraints.NotNull;

public class CartUpdateDTO {
    @NotNull(message = "购物车ID不能为空")
    private Long id;

    @NotNull(message = "数量不能为空")
    private Integer quantity;

    private Integer selected;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }
}
