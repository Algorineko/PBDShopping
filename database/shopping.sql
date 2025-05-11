/*
 Navicat Premium Data Transfer

 Source Server         : 传统链接2
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : shopping

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 11/05/2025 17:11:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `AdminID` int NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `AdminName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '管理员姓名',
  `Password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '登录密码',
  PRIMARY KEY (`AdminID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `CartID` int NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `CustomerID` int NOT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `CreatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动记录该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动记录该记录的修改时间',
  PRIMARY KEY (`CartID`) USING BTREE,
  INDEX `CustomerID`(`CustomerID` ASC) USING BTREE,
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cartitem
-- ----------------------------
DROP TABLE IF EXISTS `cartitem`;
CREATE TABLE `cartitem`  (
  `CartItemID` int NOT NULL AUTO_INCREMENT COMMENT '购物车具体项目ID',
  `CartID` int NOT NULL COMMENT '购物车ID，外键，关联购物车表',
  `ProductID` int NOT NULL COMMENT '商品ID，外键，关联商品表',
  `Quantity` int NULL DEFAULT 1 COMMENT '商品数量',
  `SelectedOptions` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商品的规格(颜色、大小等，用于高端功能)',
  `Status` enum('active','removed') CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT 'active' COMMENT '状态跟踪（是不是已经被移除了，用于高端功能）',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动记录该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动记录该记录的修改时间',
  PRIMARY KEY (`CartItemID`) USING BTREE,
  UNIQUE INDEX `idx_cart_product`(`CartID` ASC, `ProductID` ASC) USING BTREE,
  INDEX `ProductID`(`ProductID` ASC) USING BTREE,
  CONSTRAINT `cartitem_ibfk_1` FOREIGN KEY (`CartID`) REFERENCES `cart` (`CartID`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `cartitem_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cartitem_chk_1` CHECK (`Quantity` > 0)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `CustomerID` int NOT NULL COMMENT '顾客ID',
  `CustomerName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '顾客名称',
  `Password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '登录密码',
  `Money` decimal(10, 2) NULL DEFAULT NULL COMMENT '余额',
  `PhoneNumber` int NULL DEFAULT NULL COMMENT '手机号码',
  `Address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '顾客地址',
  PRIMARY KEY (`CustomerID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for logisticsinfo
-- ----------------------------
DROP TABLE IF EXISTS `logisticsinfo`;
CREATE TABLE `logisticsinfo`  (
  `LogisticsID` int NOT NULL COMMENT '物流ID',
  `OrderItemID` int NOT NULL COMMENT '订单项目ID, 外键, 关联订单项表',
  `LogisticsCompany` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '物流公司',
  `TrackingNumber` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '物流单号',
  `Status` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '物流状态',
  PRIMARY KEY (`LogisticsID`) USING BTREE,
  INDEX `logisticsinfo_ibfk_1`(`OrderItemID` ASC) USING BTREE,
  CONSTRAINT `logisticsinfo_ibfk_1` FOREIGN KEY (`OrderItemID`) REFERENCES `orderitem` (`OrderItemID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant`  (
  `MerchantID` int NOT NULL COMMENT '商家ID',
  `MerchantName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '商家名称',
  `MerchantAddress` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商家地址',
  `Password` int NOT NULL COMMENT '登录密码',
  `PhoneNumber` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '联系方式',
  PRIMARY KEY (`MerchantID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orderitem
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem`  (
  `OrderItemID` int NOT NULL COMMENT '订单具体项目ID',
  `OrderID` int NULL DEFAULT NULL COMMENT '订单ID, 外键, 关联订单表',
  `ProductID` int NULL DEFAULT NULL COMMENT '商品ID, 外键, 关联商品表',
  `Quantity` int NULL DEFAULT NULL COMMENT '数量',
  `Price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  PRIMARY KEY (`OrderItemID`) USING BTREE,
  INDEX `OrderID`(`OrderID` ASC) USING BTREE,
  INDEX `ProductID`(`ProductID` ASC) USING BTREE,
  CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `orderitem_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `OrderID` int NOT NULL COMMENT '订单ID',
  `CustomerID` int NULL DEFAULT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `OrderDate` datetime NULL DEFAULT NULL COMMENT '订单日期',
  `TotalAmount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `Status` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`OrderID`) USING BTREE,
  INDEX `CustomerID`(`CustomerID` ASC) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `ProductID` int NOT NULL COMMENT '商品ID',
  `CategoryID` int NULL DEFAULT NULL COMMENT '分类ID, 外键, 关联商品分类表',
  `MerchantID` int NULL DEFAULT NULL COMMENT '商家ID, 外键, 关联商家表',
  `ProductName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商品名称',
  `Description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '商品描述',
  `Price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  PRIMARY KEY (`ProductID`) USING BTREE,
  INDEX `CategoryID`(`CategoryID` ASC) USING BTREE,
  INDEX `MerchantID`(`MerchantID` ASC) USING BTREE,
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`CategoryID`) REFERENCES `productcategory` (`CategoryID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`MerchantID`) REFERENCES `merchant` (`MerchantID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for productcategory
-- ----------------------------
DROP TABLE IF EXISTS `productcategory`;
CREATE TABLE `productcategory`  (
  `CategoryID` int NOT NULL COMMENT '分类ID',
  `CategoryName` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '分类名称',
  `Description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '分类描述',
  PRIMARY KEY (`CategoryID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for productimages
-- ----------------------------
DROP TABLE IF EXISTS `productimages`;
CREATE TABLE `productimages`  (
  `ImageID` int NOT NULL COMMENT '图片ID',
  `ProductID` int NULL DEFAULT NULL COMMENT '商品ID，外键，关联商品表',
  `image_data` mediumblob NULL COMMENT '图片数据',
  PRIMARY KEY (`ImageID`) USING BTREE,
  INDEX `ProductID`(`ProductID` ASC) USING BTREE,
  CONSTRAINT `productimages_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for returnexchangerequest
-- ----------------------------
DROP TABLE IF EXISTS `returnexchangerequest`;
CREATE TABLE `returnexchangerequest`  (
  `RequestID` int NOT NULL COMMENT '退货换货请求ID',
  `OrderItemID` int NULL DEFAULT NULL COMMENT '订单项目ID, 外键, 关联订单项表',
  `CustomerID` int NULL DEFAULT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `RequestType` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '请求类型(是退货、换货，还是没有操作)',
  `Reason` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '退货换货原因',
  `Status` tinyint(1) NULL DEFAULT NULL COMMENT '退货换货状态(即商家是否同意，值取0 / 1)',
  PRIMARY KEY (`RequestID`) USING BTREE,
  INDEX `CustomerID`(`CustomerID` ASC) USING BTREE,
  INDEX `returnexchangerequest_ibfk_1`(`OrderItemID` ASC) USING BTREE,
  CONSTRAINT `returnexchangerequest_ibfk_1` FOREIGN KEY (`OrderItemID`) REFERENCES `orderitem` (`OrderItemID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `returnexchangerequest_ibfk_2` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `ReviewID` int NOT NULL COMMENT '评价ID',
  `CustomerID` int NULL DEFAULT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `OrderItemID` int NULL DEFAULT NULL COMMENT '订单项目ID, 外键, 关联订单项表',
  `Rating` decimal(2, 2) NULL DEFAULT NULL COMMENT '评分',
  `Comment` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '评论',
  `ReviewDate` datetime NULL DEFAULT NULL COMMENT '评价日期',
  PRIMARY KEY (`ReviewID`) USING BTREE,
  INDEX `CustomerID`(`CustomerID` ASC) USING BTREE,
  INDEX `review_ibfk_3`(`OrderItemID` ASC) USING BTREE,
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_ibfk_3` FOREIGN KEY (`OrderItemID`) REFERENCES `orderitem` (`OrderItemID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
