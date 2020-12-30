-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: vhr
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asset`
--

DROP TABLE IF EXISTS `asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资产编号',
  `status` int NOT NULL DEFAULT '1' COMMENT '资产状态：1：申请中；2：使用中；3：故障；4：报废；5：审批未通过；6：闲置',
  `statusName` varchar(255) DEFAULT NULL COMMENT '资产状态名称',
  `assetName` varchar(255) DEFAULT NULL COMMENT '资产名称',
  `type` varchar(10) DEFAULT NULL COMMENT '资产类型',
  `brand` varchar(10) DEFAULT NULL COMMENT '品牌',
  `price` double DEFAULT NULL COMMENT '价格',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `adv` json DEFAULT NULL COMMENT '详细信息',
  `readyDate` timestamp NULL DEFAULT NULL COMMENT '入库时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '资产数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset_pics`
--

DROP TABLE IF EXISTS `asset_pics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_pics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `aid` bigint DEFAULT NULL COMMENT '资产id',
  `name` varchar(255) DEFAULT NULL COMMENT '图片名称',
  `url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `createTime` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset_types`
--

DROP TABLE IF EXISTS `asset_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_types` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '类型名称',
  `createTime` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_types`
--

LOCK TABLES `asset_types` WRITE;
/*!40000 ALTER TABLE `asset_types` DISABLE KEYS */;
INSERT INTO `asset_types` VALUES (1,'手机','2020-11-08'),(2,'主机','2020-11-08'),(3,'交换机','2020-11-08'),(4,'测距仪','2020-11-08'),(5,'书籍资料','2020-11-08'),(6,'摄像机','2020-11-08'),(7,'激光雷达','2020-11-08'),(8,'内存条','2020-11-08'),(9,'转接线','2020-11-08'),(10,'快充插头','2020-11-08'),(11,'移动硬盘','2020-11-08'),(12,'电瓶','2020-11-08'),(13,'其他','2020-11-08');
/*!40000 ALTER TABLE `asset_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user`
--

DROP TABLE IF EXISTS `lams_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lams_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `phone` char(11) DEFAULT NULL COMMENT '手机号码',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名，实际是邮箱',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `enabled` tinyint(1) DEFAULT '1',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user`
--

LOCK TABLES `lams_user` WRITE;
/*!40000 ALTER TABLE `lams_user` DISABLE KEYS */;
INSERT INTO `lams_user` VALUES (3,'系统管理员','18568887789','admin','$2a$10$ySG2lkvjFHY5O0./CPIE1OI8VJsuKYEzOYzqIa7AJR6sEgSzUFOAm',1,'系统管理员'),(13,'hhj','18840833079','294116824@qq.com','$2a$10$F9ccPpocdnltlEPTnUfI5OHD2iXXov6pWTwkYiSgvqyWzSTb72PjK',0,NULL);
/*!40000 ALTER TABLE `lams_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_role`
--

DROP TABLE IF EXISTS `lams_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lams_user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int DEFAULT NULL,
  `rid` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rid` (`rid`),
  KEY `hr_role_ibfk_1` (`uid`),
  CONSTRAINT `lams_user_role_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `lams_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `lams_user_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user_role`
--

LOCK TABLES `lams_user_role` WRITE;
/*!40000 ALTER TABLE `lams_user_role` DISABLE KEYS */;
INSERT INTO `lams_user_role` VALUES (1,3,6),(83,13,24),(84,3,22),(85,3,23),(86,14,24);
/*!40000 ALTER TABLE `lams_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(64) DEFAULT NULL,
  `path` varchar(64) DEFAULT NULL,
  `component` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `iconCls` varchar(64) DEFAULT NULL,
  `keepAlive` tinyint(1) DEFAULT NULL,
  `requireAuth` tinyint(1) DEFAULT NULL,
  `parentId` int DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `parentId` (`parentId`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'/',NULL,NULL,'所有',NULL,NULL,NULL,NULL,1),(2,'/','/home','Home','资产概况','fa fa-dashboard',NULL,1,1,1),(3,'/','/home','Home','资产管理','fa fa-laptop',NULL,1,1,1),(4,'/','/home','Home','我的','fa fa-address-card-o',NULL,1,1,1),(5,'/','/home','Home','订单管理','fa fa-shopping-cart',NULL,1,1,1),(6,'/','/home','Home','系统管理','fa fa-windows',NULL,1,1,1),(7,'/asset/dashboard/**','/asset/dashboard','AssetDashBoard','仪表盘',NULL,NULL,1,2,1),(8,'/order/in/**','/order/in','AssetIn','资产入库',NULL,NULL,1,3,1),(9,'/order/out/**','/order/out','AssetOut','出库借用',NULL,NULL,1,3,1),(10,'/order/return/**','/order/return','AssetReturn','资产归还',NULL,NULL,1,3,1),(11,'/asset/info/**','/asset/info','AssetInfo','资产信息',NULL,NULL,1,3,1),(12,'/asset/repair/**','/asset/repair','AssetRepair','资产报修',NULL,NULL,1,3,1),(13,'/asset/clean/**','/asset/clean','AssetClean','清理报废',NULL,NULL,1,3,1),(14,'/mine/apply/**','/mine/apply','MyApply','我的申请',NULL,NULL,1,4,1),(15,'/mine/task/**','/mine/task','MyTask','我的任务',NULL,NULL,1,4,1),(16,'/mine/out/**','/mine/out','MyOut','离退流程',NULL,NULL,1,4,1),(17,'/system/permission/**','/sys/permission','SysPermission','权限管理',NULL,NULL,1,6,1),(18,'/system/log/**','/sys/log','SysLog','日志管理',NULL,NULL,1,6,1),(19,'/system/user/**','/sys/user','SysUser','用户管理',NULL,NULL,1,6,1),(20,'/purchase/info/**','/purchase/info','PurchaseOrder','订单信息',NULL,NULL,1,5,1);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_role`
--

DROP TABLE IF EXISTS `menu_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mid` int DEFAULT NULL,
  `rid` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mid` (`mid`),
  KEY `rid` (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=362 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_role`
--

LOCK TABLES `menu_role` WRITE;
/*!40000 ALTER TABLE `menu_role` DISABLE KEYS */;
INSERT INTO `menu_role` VALUES (1,7,6),(2,8,6),(3,9,6),(4,10,6),(5,11,6),(6,12,6),(7,13,6),(8,14,6),(9,15,6),(10,16,6),(11,17,6),(12,18,6),(13,19,6),(14,20,6),(329,7,22),(330,8,22),(331,9,22),(332,10,22),(333,11,22),(334,12,22),(335,13,22),(336,14,22),(337,15,22),(338,16,22),(339,20,22),(340,17,22),(341,18,22),(342,19,22),(343,7,23),(344,8,23),(345,9,23),(346,10,23),(347,11,23),(348,12,23),(349,14,23),(350,15,23),(351,16,23),(352,20,23),(353,7,24),(354,8,24),(355,9,24),(356,10,24),(357,11,24),(358,12,24),(359,14,24),(360,15,24),(361,16,24);
/*!40000 ALTER TABLE `menu_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '工单id',
  `category` int DEFAULT NULL COMMENT '流程类型：1.入库，2.出库，3.离退',
  `categoryName` varchar(255) DEFAULT NULL COMMENT '流程名称',
  `status` int DEFAULT NULL COMMENT '状态类型：1：申请采购；2：审批通过；3：已入库；4：申请借用；5：已借出；6：审批未通过；7：已关闭',
  `statusName` varchar(10) DEFAULT NULL COMMENT '状态名称',
  `expireTime` timestamp NULL DEFAULT NULL COMMENT '预计归还时间（过期时间）',
  `reason` varchar(255) DEFAULT NULL COMMENT '理由',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '申请人邮箱',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` date DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_asset`
--

DROP TABLE IF EXISTS `order_asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_asset` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oid` bigint DEFAULT NULL,
  `aid` bigint DEFAULT NULL COMMENT '资产流程类型：1.入库，2.出库',
  `createTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_workflow`
--

DROP TABLE IF EXISTS `order_workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_workflow` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oid` varchar(10) DEFAULT NULL COMMENT '工单id',
  `workflow_inst_id` bigint DEFAULT NULL COMMENT '工作流实例id',
  `workflow_start_time` date DEFAULT NULL COMMENT '工作流开始时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Table structure for table `process_category`
--

DROP TABLE IF EXISTS `process_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process_category` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category` int DEFAULT NULL COMMENT '流程类型',
  `categoryName` varchar(255) DEFAULT NULL COMMENT '流程名称',
  `workflowKey` varchar(255) DEFAULT NULL COMMENT '工作流key',
  `creator` varchar(10) DEFAULT NULL COMMENT '创建人',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_category`
--

LOCK TABLES `process_category` WRITE;
/*!40000 ALTER TABLE `process_category` DISABLE KEYS */;
INSERT INTO `process_category` VALUES (1,1,'资产入库','assetIn','胡宏建','2020-09-24'),(2,2,'资产出库','assetOut','胡宏建','2020-09-26'),(3,3,'学生离退','studentOut','胡宏建','2020-10-28'),(4,4,'资产归还','assetReturn','胡宏建','2020-12-29');
/*!40000 ALTER TABLE `process_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_asset`
--

DROP TABLE IF EXISTS `purchase_asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_asset` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `poid` bigint DEFAULT NULL COMMENT '订单id',
  `aid` bigint DEFAULT NULL COMMENT '资产id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '订单名称',
  `total` double DEFAULT NULL COMMENT '订单总价',
  `discount` double DEFAULT NULL COMMENT '订单优惠',
  `pay` double DEFAULT NULL COMMENT '实际支付',
  `purchaseDate` date DEFAULT NULL COMMENT '购买时间',
  `hasInvoice` tinyint(1) DEFAULT NULL COMMENT '是否有发票',
  `invoiceDate` date DEFAULT NULL COMMENT '发票时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `creatorEmail` varchar(255) DEFAULT NULL COMMENT '创建人邮箱',
  `updaterEmail` varchar(255) DEFAULT NULL COMMENT '更新人邮箱',
  `createTime` date DEFAULT NULL COMMENT '创建时间',
  `updateTime` date DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_pics`
--

DROP TABLE IF EXISTS `purchase_pics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_pics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `poid` bigint DEFAULT NULL COMMENT '订单id',
  `name` varchar(255) DEFAULT NULL COMMENT '图片名称',
  `url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `createTime` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operate` int DEFAULT NULL COMMENT '操作类型',
  `operateName` varchar(255) DEFAULT NULL COMMENT '操作名称',
  `operatorName` varchar(255) DEFAULT NULL COMMENT '操作人',
  `operatorMail` varchar(255) DEFAULT NULL COMMENT '操作人邮箱',
  `text` varchar(255) DEFAULT NULL COMMENT '操作内容',
  `operateTime` date DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `nameZh` varchar(64) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (6,'ROLE_admin','系统管理员'),(22,'ROLE_teacher','教师'),(23,'ROLE_accountant','财务'),(24,'ROLE_student','学生');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-31  2:36:28
