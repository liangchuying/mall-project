package com.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

@TableName("category")
@Schema(description = "商品分类实体")
public class Category extends BaseEntity {

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "父分类ID，0表示顶级分类")
    private Long parentId;

    @Schema(description = "分类层级：1=顶级，2=二级，3=三级")
    private Integer level;

    @Schema(description = "分类图标")
    private String icon;

    @Schema(description = "分类排序号")
    private Integer sort;

    @Schema(description = "分类状态：0=禁用，1=启用")
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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
