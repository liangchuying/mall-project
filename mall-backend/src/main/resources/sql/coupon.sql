-- 优惠券表
CREATE TABLE IF NOT EXISTS `coupon` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    `type` TINYINT NOT NULL DEFAULT 1 COMMENT '优惠券类型：1=满减券，2=折扣券，3=立减券',
    `discount_amount` DECIMAL(10,2) COMMENT '优惠金额（满减券、立减券）',
    `discount_rate` DECIMAL(5,2) COMMENT '折扣率（折扣券，如0.8表示8折）',
    `min_amount` DECIMAL(10,2) COMMENT '最低消费金额',
    `total_count` INT NOT NULL COMMENT '发放总量',
    `used_count` INT NOT NULL DEFAULT 0 COMMENT '已使用数量',
    `receive_count` INT NOT NULL DEFAULT 0 COMMENT '已领取数量',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0=已下架，1=进行中，2=已结束',
    `valid_days` INT COMMENT '有效天数',
    `valid_start_time` DATETIME COMMENT '有效期开始时间',
    `valid_end_time` DATETIME COMMENT '有效期结束时间',
    `description` VARCHAR(500) COMMENT '使用说明',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除，1=已删除',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_valid_time` (`valid_start_time`, `valid_end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 用户优惠券表
CREATE TABLE IF NOT EXISTS `user_coupon` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
    `coupon_code` VARCHAR(64) NOT NULL COMMENT '优惠券编码',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0=未使用，1=已使用，2=已过期',
    `receive_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    `use_time` DATETIME COMMENT '使用时间',
    `order_no` VARCHAR(64) COMMENT '使用的订单号',
    `valid_time` DATETIME COMMENT '有效期',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_coupon_code` (`coupon_code`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_coupon_id` (`coupon_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';
