-- 库存变动记录表
CREATE TABLE IF NOT EXISTS `stock_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
    `type` TINYINT NOT NULL COMMENT '变动类型：1=入库，2=出库，3=锁定，4=解锁，5=扣减',
    `quantity` INT NOT NULL COMMENT '变动数量（正数）',
    `before_quantity` INT NOT NULL COMMENT '变动前库存',
    `after_quantity` INT NOT NULL COMMENT '变动后库存',
    `order_no` VARCHAR(64) COMMENT '关联订单号',
    `remark` VARCHAR(255) COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_sku_id` (`sku_id`),
    KEY `idx_type` (`type`),
    KEY `idx_order_no` (`order_no`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存变动记录表';
