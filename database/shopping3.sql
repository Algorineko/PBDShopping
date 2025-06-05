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

 Date: 18/05/2025 19:22:57
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, '真·李浩', '123456');

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
  INDEX `cart_1`(`CustomerID` ASC) USING BTREE,
  CONSTRAINT `cart_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------

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
  INDEX `cartitem_ibfk_2`(`ProductID` ASC) USING BTREE,
  CONSTRAINT `cartitem_ibfk_1` FOREIGN KEY (`CartID`) REFERENCES `cart` (`CartID`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `cartitem_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cartitem_chk_1` CHECK (`Quantity` > 0)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cartitem
-- ----------------------------

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
  PRIMARY KEY (`CustomerID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1, '张三', '123456', 100.50, '110101010', '北京市朝阳区', NULL, '2025-05-18 16:09:54', '2025-05-18 18:27:57');
INSERT INTO `customer` VALUES (2, '李四', '123456', 200.75, '200202020', '上海市浦东新区', NULL, '2025-05-18 16:09:54', '2025-05-18 18:27:59');
INSERT INTO `customer` VALUES (3, '李浩3', '123456', 1314520.25, '5273461', '离开家很过分打撒', NULL, '2025-05-18 16:09:54', '2025-05-18 18:28:02');
INSERT INTO `customer` VALUES (4, '赵六', '123456', 300.25, '4040400440', '深圳市南山区', NULL, '2025-05-18 16:09:54', '2025-05-18 18:28:04');
INSERT INTO `customer` VALUES (5, '孙七', '123456', 50.00, '550050505', '成都市武侯区', NULL, '2025-05-18 16:09:54', '2025-05-18 18:28:05');
INSERT INTO `customer` VALUES (6, '李浩1', '123456', 114514.14, '666666666', '哦哈哈哇哈哈哈', NULL, '2025-05-18 16:09:54', '2025-05-18 18:28:37');
INSERT INTO `customer` VALUES (7, '李浩2', '123456', 114514.14, '1324765', '阿斯顿发过火就看了', NULL, '2025-05-18 16:09:54', '2025-05-18 18:28:16');

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
  INDEX `logisticsinfo_1`(`OrderItemID` ASC) USING BTREE,
  CONSTRAINT `logisticsinfo_1` FOREIGN KEY (`OrderItemID`) REFERENCES `orderitem` (`OrderItemID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of logisticsinfo
-- ----------------------------

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant`  (
  `MerchantID` int NOT NULL AUTO_INCREMENT COMMENT '商家ID',
  `MerchantName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '商家名称',
  `MerchantAddress` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商家地址',
  `Password` int NOT NULL COMMENT '登录密码',
  `PhoneNumber` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '联系方式',
  `HeadPicture` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '头像',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`MerchantID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of merchant
-- ----------------------------
INSERT INTO `merchant` VALUES (1, '商家A', '上海市浦东新区', 123456, '13800138000', NULL, '2025-05-18 18:42:24', '2025-05-18 18:42:24');
INSERT INTO `merchant` VALUES (2, '商家B', '北京市朝阳区', 123456, '13900139000', NULL, '2025-05-18 18:42:24', '2025-05-18 18:42:59');
INSERT INTO `merchant` VALUES (3, '商家C', '广州市天河区', 123456, '13700137000', NULL, '2025-05-18 18:42:24', '2025-05-18 18:43:07');
INSERT INTO `merchant` VALUES (4, '商家D', '深圳市南山区', 123456, '13600136000', NULL, '2025-05-18 18:42:24', '2025-05-18 18:43:13');
INSERT INTO `merchant` VALUES (5, '商家E', '杭州市滨江区', 123456, '13500135000', NULL, '2025-05-18 18:42:24', '2025-05-18 18:43:16');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orderitem
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `OrderID` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `CustomerID` int NULL DEFAULT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `OrderDate` datetime NULL DEFAULT NULL COMMENT '订单日期',
  `TotalPrice` double(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `Status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '订单状态(PENDING,PAID,SHIPPED,DELIVERED,RETURNED)',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`OrderID`) USING BTREE,
  INDEX `orders_1`(`CustomerID` ASC) USING BTREE,
  CONSTRAINT `orders_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `ProductID` int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `CategoryID` int NULL DEFAULT NULL COMMENT '分类ID, 外键, 关联商品分类表',
  `MerchantID` int NULL DEFAULT NULL COMMENT '商家ID, 外键, 关联商家表',
  `ProductName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商品名称',
  `Description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '商品描述',
  `Price` double(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`ProductID`) USING BTREE,
  INDEX `product_ibfk_1`(`CategoryID` ASC) USING BTREE,
  INDEX `product_ibfk_2`(`MerchantID` ASC) USING BTREE,
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`CategoryID`) REFERENCES `productcategory` (`CategoryID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`MerchantID`) REFERENCES `merchant` (`MerchantID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 1, 1, 'iPhone 15 Pro Max', '6.7英寸超视网膜XDR显示屏，A17 Pro芯片', 9999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (2, 1, 2, '华为Mate60 Pro+', '昆仑玻璃二代+玄武机身，卫星通信功能', 8999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (3, 1, 3, '小米14 Ultra', '徕卡光学系统，双向卫星通信', 6499.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (4, 2, 4, 'MacBook Pro M3 Max', '16英寸Liquid视网膜XDR显示屏，120Hz刷新率', 24999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (5, 2, 5, '联想ThinkPad X1 Carbon Gen11', '14英寸2.8K OLED屏幕，32GB内存', 12999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (6, 2, 1, '戴尔XPS15笔记本', '15.6英寸4K触控屏，i9-13900H处理器', 18999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (7, 3, 2, '格力云佳空调1.5匹', '新一级能效，56℃净菌自洁系统', 3299.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (8, 3, 3, '美的微晶冰箱501L', '一级能效双变频，PST+智能除菌', 5999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (9, 3, 4, '索尼75X90L电视', '75英寸4K HDR，XR认知芯片', 12999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (10, 4, 5, '慕思乳胶床垫1.8米', '独立袋装弹簧+天然乳胶层', 8999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (11, 4, 1, '水星家纺四件套纯棉', '100%新疆长绒棉，活性印染工艺', 599.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (12, 5, 2, '波司登极寒系列羽绒服', '-30℃抗寒科技，800+蓬松度鹅绒', 1999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (13, 5, 3, '优衣库U系列羊毛大衣女款', '100%澳洲美丽诺羊毛面料', 999.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (14, 6, 4, 'LV Neverfull中号手袋', 'Monogram老花帆布材质，配可拆卸内袋', 18500.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (15, 6, 5, '劳力士Datejust日志型腕表', '41毫米不锈钢表壳，纪念型表带', 65900.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (16, 7, 1, '雅诗兰黛小棕瓶精华100ml', '特润修护肌透精华露第七代', 1150.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (17, 7, 2, 'SK-II神仙水230ml套装', 'PITERA™精华护肤套装限量版', 1790.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (18, 8, 3, '五粮液52度普五第八代500ml*2礼盒装', '', 2698.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (19, 8, 4, '智利车厘子JJ级5kg礼盒装', '', 399.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');
INSERT INTO `product` VALUES (20, 11, 5, 'YONEX天斧100ZZ羽毛球拍专业版', '', 1880.00, '2025-05-18 18:55:02', '2025-05-18 18:55:02');

-- ----------------------------
-- Table structure for productcategory
-- ----------------------------
DROP TABLE IF EXISTS `productcategory`;
CREATE TABLE `productcategory`  (
  `CategoryID` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `CategoryName` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '分类名称',
  `Description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '分类描述',
  `Image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '分类图标',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`CategoryID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of productcategory
-- ----------------------------
INSERT INTO `productcategory` VALUES (1, '手机/运营商/数码', '智能手机、配件及运营商业务', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (2, '电脑/办公', '笔记本电脑、台式机、办公设备', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (3, '家用电器', '大家电、厨房电器、生活电器', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (4, '家居/家装/家纺', '家具、家装饰品、床上用品', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (5, '男装/女装/内衣', '潮流服饰、内衣配饰', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (6, '鞋靴/箱包/钟表', '运动鞋、箱包、腕表', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (7, '美妆/个护清洁', '护肤品、彩妆、个人护理', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (8, '食品/酒类/生鲜', '零食、酒水、新鲜食材', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (9, '母婴/玩具/童装', '奶粉、玩具、儿童服饰', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (10, '医药保健/医疗器械', '药品、保健品、医疗设备', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (11, '运动/户外/乐器', '运动装备、户外用品、乐器', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');
INSERT INTO `productcategory` VALUES (12, '汽车/二手车/用品', '整车、二手车、汽车配件', NULL, '2025-05-18 18:47:31', '2025-05-18 18:47:31');

-- ----------------------------
-- Table structure for productimages
-- ----------------------------
DROP TABLE IF EXISTS `productimages`;
CREATE TABLE `productimages`  (
  `ImageID` int NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `ProductID` int NULL DEFAULT NULL COMMENT '商品ID，外键，关联商品表',
  `Image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '图片数据，此处用varchar以供vue渲染',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`ImageID`) USING BTREE,
  INDEX `productimages_1`(`ProductID` ASC) USING BTREE,
  CONSTRAINT `productimages_1` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of productimages
-- ----------------------------

-- ----------------------------
-- Table structure for returnexchangerequest
-- ----------------------------
DROP TABLE IF EXISTS `returnexchangerequest`;
CREATE TABLE `returnexchangerequest`  (
  `RequestID` int NOT NULL AUTO_INCREMENT COMMENT '退货换货请求ID',
  `OrderItemID` int NULL DEFAULT NULL COMMENT '订单项目ID, 外键, 关联订单项表',
  `CustomerID` int NULL DEFAULT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `RequestType` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '请求类型(是退货、换货，还是没有操作)',
  `Reason` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '退货换货原因',
  `Status` tinyint(1) NULL DEFAULT NULL COMMENT '退货换货状态(即商家是否同意，值取0 / 1)',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`RequestID`) USING BTREE,
  INDEX `returnexchangerequest_ibfk_1`(`OrderItemID` ASC) USING BTREE,
  INDEX `returnexchangerequest_ibfk_2`(`CustomerID` ASC) USING BTREE,
  CONSTRAINT `returnexchangerequest_ibfk_1` FOREIGN KEY (`OrderItemID`) REFERENCES `orderitem` (`OrderItemID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `returnexchangerequest_ibfk_2` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of returnexchangerequest
-- ----------------------------

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `ReviewID` int NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `CustomerID` int NULL DEFAULT NULL COMMENT '顾客ID, 外键, 关联顾客表',
  `OrderItemID` int NULL DEFAULT NULL COMMENT '订单项目ID, 外键, 关联订单项表',
  `Rating` decimal(2, 2) NULL DEFAULT NULL COMMENT '评分（0.0-100.0）',
  `Comment` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '评论',
  `AddedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '可自动登记该记录的创建时间',
  `UpdatedAt` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '可自动登记该记录的修改时间',
  PRIMARY KEY (`ReviewID`) USING BTREE,
  INDEX `review_ibfk_2`(`OrderItemID` ASC) USING BTREE,
  INDEX `review_ibfk_1`(`CustomerID` ASC) USING BTREE,
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_ibfk_2` FOREIGN KEY (`OrderItemID`) REFERENCES `orderitem` (`OrderItemID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
