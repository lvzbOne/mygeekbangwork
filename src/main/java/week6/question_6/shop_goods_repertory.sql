/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : electronic_commerce

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 10/04/2022 23:12:35
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for shop_goods_repertory
-- ----------------------------
-- DROP TABLE IF EXISTS `shop_goods_repertory`;
CREATE TABLE IF NOT EXISTS `shop_goods_repertory`
(
    `id` bigint
(
    20
) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '店铺商品库存物理id自增',
    `shop_code` varchar
(
    32
) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '店铺编码',
    `goods_code` varchar
(
    32
) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品编码',
    `goods_num` int
(
    10
) NOT NULL DEFAULT 1 COMMENT '商品库存数量',
    `price` decimal
(
    12,
    4
) NOT NULL DEFAULT 0.0000 COMMENT '该店铺商品价格',
    `store_date` int
(
    8
) NOT NULL COMMENT '入库日期格式yyyyMMdd',
    `gmt_create` datetime
(
    0
) NULL DEFAULT CURRENT_TIMESTAMP
(
    0
) COMMENT '创建时间',
    `gmt_modified` datetime
(
    0
) NULL DEFAULT CURRENT_TIMESTAMP
(
    0
) COMMENT '修改时间',
    PRIMARY KEY
(
    `id`
) USING BTREE,
    UNIQUE INDEX `uk_s_g`
(
    `shop_code`,
    `goods_code`
)
  USING BTREE
    ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '店铺商品库存表' ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
