package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "更新商品分类请求")
public class CategoryUpdateDTO {

    @Schema(description = "分类ID", required = true, example = "1")
    @NotNull(message = "分类ID不能为空")
    private Long id;

    @Schema(description = "分类名称", example = "手机")
    private String name;

    @Schema(description = "分类图标", example = "icon-phone.png")
    private String icon;

    @Schema(description = "分类排序号", example = "1")
    @Min(value = 0, message = "排序号不能小于0")
    private Integer sort;

    @Schema(description = "分类状态：0=禁用，1=启用", example = "1")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
