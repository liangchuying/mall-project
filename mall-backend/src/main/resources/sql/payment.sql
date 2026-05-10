-- 支付记录表
CREATE TABLE IF NOT EXISTS `payment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `payment_no` VARCHAR(64) NOT NULL COMMENT '支付流水号',
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `payment_type` VARCHAR(20) NOT NULL COMMENT '支付方式：ALIPAY=支付宝，WECHAT=微信支付',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态：0=待支付，1=支付成功，2=支付失败，3=已退款',
    `transaction_id` VARCHAR(128) COMMENT '第三方支付交易号',
    `pay_time` DATETIME COMMENT '支付时间',
    `notify_time` DATETIME COMMENT '回调通知时间',
    `remark` VARCHAR(500) COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_no` (`payment_no`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_payment_type` (`payment_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';
