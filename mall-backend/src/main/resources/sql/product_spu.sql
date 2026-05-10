-- 商品SPU表
CREATE TABLE IF NOT EXISTS `product_spu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `category_id` BIGINT NOT NULL COMMENT '商品分类ID',
    `brand_id` BIGINT NOT NULL DEFAULT 0 COMMENT '品牌ID',
    `unit` VARCHAR(20) NOT NULL DEFAULT '件' COMMENT '单位',
    `description` TEXT COMMENT '商品描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '商品状态：0=下架，1=上架',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除，1=已删除',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_brand_id` (`brand_id`),
    KEY `idx_status` (`status`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SPU表';

-- 商品SKU表
CREATE TABLE IF NOT EXISTS `product_sku` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `spu_id` BIGINT NOT NULL COMMENT 'SPU ID',
    `sku_code` VARCHAR(100) NOT NULL COMMENT 'SKU编码',
    `specs` JSON COMMENT '规格属性JSON（如：{"颜色":"红色","尺寸":"XL"}）',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'SKU状态：0=下架，1=上架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除，1=已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_code` (`sku_code`),
    KEY `idx_spu_id` (`spu_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- 商品品牌表
CREATE TABLE IF NOT EXISTS `product_brand` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '品牌名称',
    `logo` VARCHAR(255) COMMENT '品牌Logo',
    `description` VARCHAR(500) COMMENT '品牌描述',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '品牌状态：0=禁用，1=启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除，1=已删除',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品品牌表';

-- 商品图片表
CREATE TABLE IF NOT EXISTS `product_image` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `spu_id` BIGINT NOT NULL COMMENT 'SPU ID',
    `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
    `is_main` TINYINT NOT NULL DEFAULT 0 COMMENT '是否主图：0=否，1=是',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除，1=已删除',
    PRIMARY KEY (`id`),
    KEY `idx_spu_id` (`spu_id`),
    KEY `idx_is_main` (`is_main`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品图片表';
