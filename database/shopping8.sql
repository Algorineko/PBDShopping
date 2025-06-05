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

 Date: 01/06/2025 19:05:37
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
  PRIMARY KEY (`AdminID`) USING BTREE,
  UNIQUE INDEX `用户名和密码不能重复`(`AdminName` ASC, `Password` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;


-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '123456');

-- ----------------------------
-- Table structure for cartitem
-- ----------------------------
DROP TABLE IF EXISTS `cartitem`;
CREATE TABLE `cartitem`  (
  `CartItemID` int NOT NULL AUTO_INCREMENT COMMENT '购物车具体项目ID',
  `CustomerID` int NOT NULL COMMENT '顾客ID，外键，关联购物车表',
  `ProductID` int NOT NULL COMMENT '商品ID，外键，关联商品表',
  `Quantity` int NULL DEFAULT 1 COMMENT '商品数量',
  `SelectedOptions` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商品的规格(颜色、大小等，用于高端功能)',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动记录该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动记录该记录的修改时间',
  PRIMARY KEY (`CartItemID`) USING BTREE,
  UNIQUE INDEX `idx_cart_product`(`CustomerID` ASC, `ProductID` ASC) USING BTREE,
  INDEX `cartitem_ibfk_2`(`ProductID` ASC) USING BTREE,
  CONSTRAINT `cartitem_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cartitem_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cartitem_chk_1` CHECK (`Quantity` > 0)
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `CustomerID` int NOT NULL AUTO_INCREMENT COMMENT '顾客ID',
  `CustomerName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '顾客名称',
  `Password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '登录密码',
  `Money` double(10, 2) NULL DEFAULT NULL COMMENT '余额',
  `PhoneNumber` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '手机号码',
  `Address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '顾客地址',
  `HeadPicture` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '头像',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  PRIMARY KEY (`CustomerID`) USING BTREE,
  UNIQUE INDEX `用户名和密码不能重复`(`CustomerName` ASC, `Password` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for logisticsinfo
-- ----------------------------
DROP TABLE IF EXISTS `logisticsinfo`;
CREATE TABLE `logisticsinfo`  (
  `LogisticsID` int NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `OrderItemID` int NOT NULL COMMENT '订单项目ID, 外键, 关联订单项表',
  `LogisticsCompany` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '物流公司',
  `TrackingNumber` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '物流单号',
  `Status` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '物流状态',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`LogisticsID`) USING BTREE,
  UNIQUE INDEX `logisticsinfo_1`(`OrderItemID` ASC) USING BTREE,
  CONSTRAINT `logisticsinfo_1` FOREIGN KEY (`OrderItemID`) REFERENCES `orderitem` (`OrderItemID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant`  (
  `MerchantID` int NOT NULL AUTO_INCREMENT COMMENT '商家ID',
  `MerchantName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '商家名称',
  `MerchantAddress` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商家地址',
  `Password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT '登录密码',
  `PhoneNumber` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '联系方式',
  `HeadPicture` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '头像',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`MerchantID`) USING BTREE,
  UNIQUE INDEX `用户名和密码不能重复`(`MerchantName` ASC, `Password` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orderitem
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem`  (
  `OrderItemID` int NOT NULL AUTO_INCREMENT COMMENT '订单具体项目ID',
  `OrderID` int NULL DEFAULT NULL COMMENT '订单ID, 外键, 关联订单表',
  `ProductID` int NULL DEFAULT NULL COMMENT '商品ID, 外键, 关联商品表(可以关联到商家ID，故此处不登记商家ID)',
  `Quantity` int NULL DEFAULT NULL COMMENT '数量',
  `Price` double(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`OrderItemID`) USING BTREE,
  INDEX `orderitem_2`(`ProductID` ASC) USING BTREE,
  INDEX `orderitem_1`(`OrderID` ASC) USING BTREE,
  CONSTRAINT `orderitem_1` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `orderitem_2` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `OrderID` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `CustomerID` int NULL DEFAULT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `MerchantID` int NULL DEFAULT NULL COMMENT '商家ID',
  `TotalPrice` double(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `Status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '订单状态(PENDING,PAID,SHIPPED,DELIVERED,RETURNED)',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`OrderID`) USING BTREE,
  INDEX `orders_1`(`CustomerID` ASC) USING BTREE,
  INDEX `orders_2`(`MerchantID` ASC) USING BTREE,
  CONSTRAINT `orders_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `orders_2` FOREIGN KEY (`MerchantID`) REFERENCES `merchant` (`MerchantID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `ProductID` int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `CategoryID` int NULL DEFAULT NULL COMMENT '分类ID, 外键, 关联商品分类表',
  `MerchantID` int NULL DEFAULT NULL COMMENT '商家ID, 外键, 关联商家表',
  `ProductName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商品名称',
  `Description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商品描述',
  `Price` double(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`ProductID`) USING BTREE,
  INDEX `product_ibfk_1`(`CategoryID` ASC) USING BTREE,
  INDEX `product_ibfk_2`(`MerchantID` ASC) USING BTREE,
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`CategoryID`) REFERENCES `productcategory` (`CategoryID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`MerchantID`) REFERENCES `merchant` (`MerchantID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for productcategory
-- ----------------------------
DROP TABLE IF EXISTS `productcategory`;
CREATE TABLE `productcategory`  (
  `CategoryID` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `CategoryName` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '分类名称',
  `Description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '分类描述',
  `Image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '分类图标',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`CategoryID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for productimage
-- ----------------------------
DROP TABLE IF EXISTS `productimage`;
CREATE TABLE `productimage`  (
  `ProductImageID` int NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `ProductID` int NULL DEFAULT NULL COMMENT '商品ID，外键，关联商品表',
  `Image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '图片数据，此处用varchar以供vue渲染',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`ProductImageID`) USING BTREE,
  INDEX `productimages_1`(`ProductID` ASC) USING BTREE,
  CONSTRAINT `productimages_1` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for returnexchange
-- ----------------------------
DROP TABLE IF EXISTS `returnexchange`;
CREATE TABLE `returnexchange`  (
  `RequestID` int NOT NULL AUTO_INCREMENT COMMENT '退货换货请求ID',
  `OrderItemID` int NOT NULL COMMENT '订单项目ID, 外键, 关联订单项表',
  `CustomerID` int NULL DEFAULT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `RequestType` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '请求类型(是退货、换货，还是没有操作)',
  `Reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '退货换货原因',
  `Status` int NULL DEFAULT NULL COMMENT '退货换货状态(即商家是否同意，值取0 / 1)',
  `LogisticsCompany` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '物流公司，与物流信息表中对应，但没有关联',
  `TrackingNumber` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '物流单号，与物流信息表中对应，但没有关联',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`RequestID`) USING BTREE,
  INDEX `returnexchangerequest_ibfk_2`(`CustomerID` ASC) USING BTREE,
  UNIQUE INDEX `（订单项ID，顾客ID）不能重复`(`OrderItemID` ASC, `CustomerID` ASC) USING BTREE,
  CONSTRAINT `returnexchange_ibfk_1` FOREIGN KEY (`OrderItemID`) REFERENCES `orderitem` (`OrderItemID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `returnexchange_ibfk_2` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `ReviewID` int NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `CustomerID` int NULL DEFAULT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `OrderItemID` int NULL DEFAULT NULL COMMENT '订单项目ID, 外键, 关联订单项表',
  `Rating` double(4, 2) NULL DEFAULT NULL COMMENT '评分（0.0-100.0）',
  `Comment` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '评论',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`ReviewID`) USING BTREE,
  INDEX `review_ibfk_2`(`OrderItemID` ASC) USING BTREE,
  INDEX `review_ibfk_1`(`CustomerID` ASC) USING BTREE,
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_ibfk_2` FOREIGN KEY (`OrderItemID`) REFERENCES `orderitem` (`OrderItemID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
