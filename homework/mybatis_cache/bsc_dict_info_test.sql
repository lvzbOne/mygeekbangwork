/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : gefrm

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 20/05/2022 10:45:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bsc_dict_info_test
-- ----------------------------
-- DROP TABLE IF EXISTS `bsc_dict_info_test`;
CREATE TABLE IF NOT EXISTS `bsc_dict_info_test`  (
  `dict_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '字典标识',
  `dict_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '字典名称',
  `dict_sitm_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '字典子项标识',
  `dict_sitm_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '字典子项名称',
  `suse_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '启用标志',
  `disp_orde` int(11) NOT NULL DEFAULT 0 COMMENT '展示顺序',
  `memo` varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '备注'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bsc_dict_info_test
-- ----------------------------
INSERT INTO `bsc_dict_info_test` VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '更新操作！');
INSERT INTO `bsc_dict_info_test` VALUES ('aaaaatest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '1');
INSERT INTO `bsc_dict_info_test` VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '更新操作！');
INSERT INTO `bsc_dict_info_test` VALUES ('aaaaatest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '1');
INSERT INTO `bsc_dict_info_test` VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '更新操作！');
INSERT INTO `bsc_dict_info_test` VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '99911', '无关联关系', '0', 190, '更新操作！');

SET FOREIGN_KEY_CHECKS = 1;
