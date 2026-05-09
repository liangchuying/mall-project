-- ================================================
-- 商城系统 - 数据库初始化脚本
-- ================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS mall_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE mall_system;

-- ================================================
-- 用户表
-- ================================================
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` INT NOT NULL DEFAULT 1 COMMENT '状态（0-禁用，1-正常）',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ================================================
-- 商品分类表（待后续实现）
-- ================================================
-- CREATE TABLE `category` (
--     `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
--     `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
--     `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID',
--     `sort` INT DEFAULT 0 COMMENT '排序',
--     `create_time` DATETIME NOT NULL COMMENT '创建时间',
--     `update_time` DATETIME NOT NULL COMMENT '更新时间',
--     `deleted` INT NOT NULL DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
--     PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- ================================================
-- 商品表（待后续实现）
-- ================================================
-- CREATE TABLE `product` (
--     `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
--     `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
--     `category_id` BIGINT NOT NULL COMMENT '分类ID',
--     `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
--     `stock` INT DEFAULT 0 COMMENT '库存数量',
--     `description` TEXT COMMENT '商品描述',
--     `create_time` DATETIME NOT NULL COMMENT '创建时间',
--     `update_time` DATETIME NOT NULL COMMENT '更新时间',
--     `deleted` INT NOT NULL DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
--     PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';
