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
-- Table structure for table `academic_fee`
--

DROP TABLE IF EXISTS `academic_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `academic_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '国内学术会议差旅费编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细类型名称',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `meetingName` varchar(255) DEFAULT NULL COMMENT '会议名称',
  `personnelType` varchar(255) DEFAULT NULL COMMENT '人员类别',
  `times` int NOT NULL DEFAULT '0' COMMENT '人次',
  `year` int NOT NULL DEFAULT '0' COMMENT '年度',
  `days` int NOT NULL DEFAULT '0' COMMENT '会期（天）',
  `registrationFee` double(16,2) DEFAULT '0.00' COMMENT '注册费',
  `transFee` double(16,2) DEFAULT '0.00' COMMENT '往返交通费（元）',
  `accommodationFee` double(16,2) DEFAULT '0.00' COMMENT '住宿及补助费（元）',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '小计（万元）',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `advisory`
--

DROP TABLE IF EXISTS `advisory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `advisory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '专家咨询费编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `personnelType` varchar(255) DEFAULT NULL COMMENT '人员种类',
  `content` varchar(255) DEFAULT NULL COMMENT '工作内容',
  `amount` int NOT NULL DEFAULT '0' COMMENT '人数',
  `standard` double(16,2) DEFAULT '0.00' COMMENT '标准（元）',
  `days` int NOT NULL DEFAULT '0' COMMENT '天数',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '小计（万元）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `userEmail` varchar(255) DEFAULT NULL COMMENT '申请人邮箱',
  `readyDate` timestamp NULL DEFAULT NULL COMMENT '入库时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '资产数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
-- Table structure for table `budget_record`
--

DROP TABLE IF EXISTS `budget_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `budget_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '设备费编号',
  `isReimburse` int DEFAULT '0' COMMENT '是否报销：0:否，1：是',
  `reimburseName` varchar(255) DEFAULT '未报销',
  `funding` double(16,2) NOT NULL COMMENT '执行金额（万元）',
  `projectId` bigint DEFAULT NULL COMMENT '科研项目id',
  `projectName` varchar(255) DEFAULT NULL COMMENT '科研项目名称',
  `planId` int DEFAULT NULL COMMENT '预算计划id',
  `planName` varchar(255) DEFAULT NULL COMMENT '预算计划名称',
  `planItemId` bigint DEFAULT NULL COMMENT '预算明细id',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '预算执行备注',
  `userEmail` varchar(255) NOT NULL COMMENT '用户邮箱',
  `executeDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `equipment_fee`
--

DROP TABLE IF EXISTS `equipment_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '设备费编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `usage` varchar(255) DEFAULT NULL COMMENT '用途',
  `equipmentName` varchar(255) DEFAULT NULL COMMENT '设备名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `amount` int NOT NULL DEFAULT '0' COMMENT '数量',
  `unitPrice` double(16,6) NOT NULL DEFAULT '0.000000' COMMENT '单价（万元）',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '合计（万元）',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','lams','SQL','V1__lams.sql',-646716814,'root','2020-10-06 08:03:19',1549,1),(2,'1.1','lams','SQL','V1.1__lams.sql',-646716814,'root','2020-10-06 13:19:53',1063,1),(3,'1.2','lams','SQL','V1.2__lams.sql',-1694339136,'root','2020-10-08 16:21:43',2142,1),(4,'1.3','lams','SQL','V1.3__lams.sql',-585837962,'root','2020-10-08 16:27:00',986,1),(5,'1.4','lams','SQL','V1.4__lams.sql',1361875013,'root','2020-11-01 07:02:49',1084,1),(6,'1.5','lams','SQL','V1.5__lams.sql',1361875013,'root','2020-11-01 11:22:10',1803,1),(7,'1.6','lams','SQL','V1.6__lams.sql',398098745,'root','2020-11-09 06:50:22',2402,1),(8,'1.7','lams','SQL','V1.7__lams.sql',398098745,'root','2020-11-12 15:47:27',2414,1),(9,'1.8','lams','SQL','V1.8__lams.sql',-1083176953,'root','2020-12-29 09:40:35',1844,1),(10,'1.9','lams','SQL','V1.9__lams.sql',-1265192024,'root','2021-05-20 15:57:25',1599,1),(11,'2.0','lams','SQL','V2.0__lams.sql',-798112949,'root','2021-08-22 03:51:46',4199,1),(12,'2.1','lams','SQL','V2.1__lams.sql',1578357386,'root','2021-08-26 03:26:15',4328,1),(13,'2.3','lams','SQL','V2.3__lams.sql',-1522542738,'root','2021-09-02 02:48:52',3521,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_types`
--

DROP TABLE IF EXISTS `item_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_types` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '类型编号',
  `name` varchar(255) NOT NULL DEFAULT '1' COMMENT '类型名称',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_types`
--

LOCK TABLES `item_types` WRITE;
/*!40000 ALTER TABLE `item_types` DISABLE KEYS */;
INSERT INTO `item_types` VALUES (1,'设备费','2021-07-11 04:41:19'),(2,'材料费','2021-07-11 04:41:19'),(3,'测试化验加工费','2021-07-11 04:41:19'),(4,'差旅/会议/国际合作与交流费','2021-07-11 04:41:19'),(5,'出版/文献/信息传播/知识产权事务费','2021-07-11 04:41:19'),(6,'劳务费','2021-07-11 04:41:19'),(7,'专家咨询费','2021-07-11 04:41:19'),(8,'其他支出','2021-07-11 04:41:19');
/*!40000 ALTER TABLE `item_types` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user`
--

LOCK TABLES `lams_user` WRITE;
/*!40000 ALTER TABLE `lams_user` DISABLE KEYS */;
INSERT INTO `lams_user` VALUES (3,'系统管理员','18568887789','admin','$2a$10$ySG2lkvjFHY5O0./CPIE1OI8VJsuKYEzOYzqIa7AJR6sEgSzUFOAm',1,'系统管理员'),(13,'hhj','18840833079','294116824@qq.com','$2a$10$F9ccPpocdnltlEPTnUfI5OHD2iXXov6pWTwkYiSgvqyWzSTb72PjK',0,NULL),(14,'lams',NULL,'3551983486@qq.com','$2a$10$.GX9xb.PBbmd7y5yOSxYE.csxTXpUutIt.gDyllfhYP90yQMpjupi',1,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user_role`
--

LOCK TABLES `lams_user_role` WRITE;
/*!40000 ALTER TABLE `lams_user_role` DISABLE KEYS */;
INSERT INTO `lams_user_role` VALUES (1,3,6),(83,13,24),(84,3,22),(85,3,23),(86,14,24),(87,14,24);
/*!40000 ALTER TABLE `lams_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `material_fee`
--

DROP TABLE IF EXISTS `material_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `material_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '材料费编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `usage` varchar(255) DEFAULT NULL COMMENT '材料用途',
  `materialName` varchar(255) DEFAULT NULL COMMENT '材料名称',
  `amount` int DEFAULT '0' COMMENT '数量',
  `specification` varchar(255) DEFAULT NULL COMMENT '规格',
  `unitPrice` double(16,6) DEFAULT '0.000000' COMMENT '单价（万元）',
  `unit` varchar(255) DEFAULT NULL COMMENT '单位',
  `subTotal` double(16,6) NOT NULL DEFAULT '0.000000' COMMENT '小计（万元）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meeting_fee`
--

DROP TABLE IF EXISTS `meeting_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '其他支出编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `content` varchar(255) DEFAULT NULL COMMENT '会议内容',
  `relation` varchar(255) DEFAULT NULL COMMENT '与课题相关性',
  `meal` double(16,2) DEFAULT '0.00' COMMENT '伙食费（人/天）',
  `otherCost` double(16,2) DEFAULT '0.00' COMMENT '其他费用（人/天）',
  `days` int NOT NULL DEFAULT '0' COMMENT '天数',
  `amount` int NOT NULL DEFAULT '0' COMMENT '参会人数',
  `meetingNum` int NOT NULL DEFAULT '0' COMMENT '会议次数',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '小计（万元）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'/',NULL,NULL,'所有',NULL,NULL,NULL,NULL,1),(2,'/','/home','Home','预算管理','fa fa-rmb',NULL,1,1,1),(3,'/','/home','Home','资产概况','fa fa-dashboard',NULL,1,1,1),(4,'/','/home','Home','资产管理','fa fa-laptop',NULL,1,1,1),(5,'/','/home','Home','我的','fa fa-address-card-o',NULL,1,1,1),(6,'/','/home','Home','订单管理','fa fa-shopping-cart',NULL,1,1,1),(7,'/','/home','Home','系统管理','fa fa-windows',NULL,1,1,1),(8,'/budget/project/**','/budget/project','BudgetProject','科研项目',NULL,NULL,1,2,1),(9,'/budget/plan/**','/budget/plan','BudgetPlan','预算计划',NULL,NULL,1,2,1),(10,'/budget/record/**','/budget/record','BudgetRecord','执行流水',NULL,NULL,1,2,1),(12,'/budget/warn/**','/budget/warn','BudgetWarn','报警记录',NULL,NULL,1,2,1),(13,'/budget/conf/**','/budget/conf','BudgetConf','报警配置',NULL,NULL,1,2,1),(14,'/asset/dashboard/**','/asset/dashboard','AssetDashBoard','仪表盘',NULL,NULL,1,3,1),(15,'/order/in/**','/order/in','AssetIn','资产入库',NULL,NULL,1,4,1),(16,'/order/out/**','/order/out','AssetOut','出库借用',NULL,NULL,1,4,1),(17,'/order/return/**','/order/return','AssetReturn','资产归还',NULL,NULL,1,4,1),(18,'/asset/info/**','/asset/info','AssetInfo','资产信息',NULL,NULL,1,4,1),(19,'/asset/repair/**','/asset/repair','AssetRepair','资产报修',NULL,NULL,1,4,1),(20,'/asset/clean/**','/asset/clean','AssetClean','清理报废',NULL,NULL,1,4,1),(21,'/mine/apply/**','/mine/apply','MyApply','我的申请',NULL,NULL,1,5,1),(22,'/mine/task/**','/mine/task','MyTask','我的任务',NULL,NULL,1,5,1),(23,'/mine/out/**','/mine/out','MyOut','离退流程',NULL,NULL,1,5,1),(24,'/system/permission/**','/sys/permission','SysPermission','权限管理',NULL,NULL,1,7,1),(25,'/system/log/**','/sys/log','SysLog','日志管理',NULL,NULL,1,7,1),(26,'/system/user/**','/sys/user','SysUser','用户管理',NULL,NULL,1,7,1),(27,'/purchase/info/**','/purchase/info','PurchaseOrder','订单信息',NULL,NULL,1,6,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=368 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_role`
--

LOCK TABLES `menu_role` WRITE;
/*!40000 ALTER TABLE `menu_role` DISABLE KEYS */;
INSERT INTO `menu_role` VALUES (1,14,6),(2,15,6),(3,16,6),(4,17,6),(5,18,6),(6,19,6),(7,20,6),(8,21,6),(9,22,6),(10,23,6),(11,24,6),(12,25,6),(13,26,6),(14,27,6),(329,14,22),(330,15,22),(331,16,22),(332,17,22),(333,18,22),(334,19,22),(335,20,22),(336,21,22),(337,22,22),(338,23,22),(339,27,22),(340,24,22),(341,25,22),(342,26,22),(343,14,23),(344,15,23),(345,16,23),(346,17,23),(347,18,23),(348,19,23),(349,21,23),(350,22,23),(351,23,23),(352,27,23),(353,14,24),(354,15,24),(355,16,24),(356,17,24),(357,18,24),(358,19,24),(359,21,24),(360,22,24),(361,23,24),(362,8,6),(363,9,6),(364,10,6),(366,12,6),(367,13,6);
/*!40000 ALTER TABLE `menu_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_content`
--

DROP TABLE IF EXISTS `message_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message_content` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预算报警内容编号',
  `projectName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '科研项目名称',
  `planName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预算计划名称',
  `itemTypeName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预算明细类型名称',
  `executeRate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前执行率',
  `needExecuteRate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '期望执行率',
  `toEmail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收人',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message_send_log`
--

DROP TABLE IF EXISTS `message_send_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message_send_log` (
  `msgId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `contentId` bigint DEFAULT NULL COMMENT '报警内容id',
  `userEmail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int DEFAULT '0' COMMENT '0发送中，1发送成功，2发送失败',
  `routeKey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `exchange` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `count` int DEFAULT '0' COMMENT '重试次数',
  `tryTime` datetime DEFAULT NULL COMMENT '第一次重试时间',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  `aid` bigint DEFAULT NULL COMMENT '资产id',
  `createTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `other_fee`
--

DROP TABLE IF EXISTS `other_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `other_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '其他支出编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `type` varchar(255) DEFAULT NULL COMMENT '类别',
  `remark` varchar(255) DEFAULT NULL COMMENT '说明',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '小计（万元）',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '劳务费编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `personnelType` varchar(255) DEFAULT NULL COMMENT '人员种类',
  `content` varchar(255) DEFAULT NULL COMMENT '工作内容',
  `amount` int NOT NULL DEFAULT '0' COMMENT '人数',
  `standard` double(16,2) DEFAULT '0.00' COMMENT '标准（元）',
  `hours` int NOT NULL DEFAULT '0' COMMENT '工作时间',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '小计（万元）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `plan`
--

DROP TABLE IF EXISTS `plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预算计划编号',
  `isDeleted` int DEFAULT '0' COMMENT '是否被删除：1是；0否',
  `planName` varchar(255) NOT NULL DEFAULT '1' COMMENT '预算计划名称',
  `projectId` bigint NOT NULL COMMENT '科研项目id',
  `funding` double(16,2) NOT NULL COMMENT '预算额度（万元）',
  `unfulfilledQuota` double(16,2) DEFAULT NULL COMMENT '未执行额度（万元）',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人邮箱',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `plan_item`
--

DROP TABLE IF EXISTS `plan_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plan_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint DEFAULT NULL COMMENT '预算计划id',
  `itid` int DEFAULT NULL COMMENT '预算明细类型',
  `iid` bigint DEFAULT NULL COMMENT '预算明细id',
  `createTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '项目编号',
  `projectName` varchar(255) NOT NULL DEFAULT '1' COMMENT '项目名称',
  `projectType` varchar(255) DEFAULT NULL COMMENT '项目类别',
  `years` int DEFAULT NULL COMMENT '项目总体执行年限',
  `funding` double(16,2) DEFAULT '0.00' COMMENT '资助额度',
  `unfulfilledQuota` double(16,2) DEFAULT '0.00' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `userEmail` varchar(255) NOT NULL COMMENT '创建人邮箱',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project_types`
--

DROP TABLE IF EXISTS `project_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_types` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '类型编号',
  `name` varchar(255) NOT NULL DEFAULT '1' COMMENT '类型名称',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_types`
--

LOCK TABLES `project_types` WRITE;
/*!40000 ALTER TABLE `project_types` DISABLE KEYS */;
INSERT INTO `project_types` VALUES (1,'A1基础研究原始创新基金','2021-07-10 16:08:30'),(2,'A2学科交叉研究基金','2021-07-10 16:08:30'),(3,'A3关键核心技术突破支持基金','2021-07-10 16:08:30'),(4,'A4国家重大科研项目培育基金','2021-07-10 16:08:30'),(5,'A5拓展垂直领域类','2021-07-10 16:08:30'),(6,'A6军民融合技术发展类','2021-07-10 16:08:30');
/*!40000 ALTER TABLE `project_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publish_fee`
--

DROP TABLE IF EXISTS `publish_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publish_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '出版/文献/信息传播/知识产权事务费编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `type` varchar(255) DEFAULT NULL COMMENT '类别',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `remark` varchar(255) DEFAULT NULL COMMENT '说明',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '小计（万元）',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `research_fee`
--

DROP TABLE IF EXISTS `research_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `research_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课题调研差旅费编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `place` varchar(255) DEFAULT NULL COMMENT '调研地点',
  `purpose` varchar(255) DEFAULT NULL COMMENT '调研目的',
  `personnelType` varchar(255) DEFAULT NULL COMMENT '人员类别',
  `times` int NOT NULL DEFAULT '0' COMMENT '人次',
  `transFee` double(16,2) DEFAULT '0.00' COMMENT '往返交通费（元）',
  `accommodationFee` double(16,2) DEFAULT '0.00' COMMENT '住宿费（元）',
  `otherFee` double(16,2) DEFAULT '0.00' COMMENT '公杂费（元）',
  `days` int NOT NULL DEFAULT '0' COMMENT '天数',
  `amount` int NOT NULL DEFAULT '0' COMMENT '参会人数',
  `meetingNum` int NOT NULL DEFAULT '0' COMMENT '会议次数',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '小计（万元）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
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

--
-- Table structure for table `testing_fee`
--

DROP TABLE IF EXISTS `testing_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `testing_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '测试化验加工费编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) DEFAULT NULL COMMENT '预算明细名称',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `content` varchar(255) DEFAULT NULL COMMENT '加工内容',
  `times` int NOT NULL DEFAULT '0' COMMENT '次数',
  `unitPrice` double(16,6) DEFAULT '0.000000' COMMENT '单价（万元）',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '小计（万元）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transportation_fee`
--

DROP TABLE IF EXISTS `transportation_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transportation_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会议/差旅/国际合作交流费编号',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) NOT NULL COMMENT '类别',
  `userEmail` varchar(255) DEFAULT NULL COMMENT '创建人',
  `remark` varchar(255) DEFAULT NULL COMMENT '说明',
  `subTotal` double(16,6) DEFAULT '0.000000' COMMENT '小计（万元）',
  `unfulfilledQuota` double(16,6) DEFAULT '0.000000' COMMENT '未执行额度',
  `executeRate` double(16,2) DEFAULT '0.00' COMMENT '执行率',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `warning_config`
--

DROP TABLE IF EXISTS `warning_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warning_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预算报警配置编号',
  `projectId` bigint DEFAULT NULL COMMENT '科研项目id',
  `planId` int DEFAULT NULL COMMENT '预算计划id',
  `itemType` int DEFAULT NULL COMMENT '预算明细类型',
  `itemTypeName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预算明细类型名称',
  `executeRate` double(16,2) DEFAULT NULL COMMENT '报警执行率',
  `configStatus` int DEFAULT NULL COMMENT '预算报警配置状态：1进行中；2暂停',
  `configStatusName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预算报警配置状态名称',
  `userEmail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `toUserString` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收人列表',
  `startTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `endTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `freq` int DEFAULT NULL COMMENT '报警频率：1每天， 2每周',
  `freqName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '报警频率名称',
  `advice` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '报警处理建议',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-09-04 21:38:46
