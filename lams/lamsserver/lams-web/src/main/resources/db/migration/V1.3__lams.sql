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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user_role`
--

LOCK TABLES `lams_user_role` WRITE;
/*!40000 ALTER TABLE `lams_user_role` DISABLE KEYS */;
INSERT INTO `lams_user_role` VALUES (1,3,6),(78,5,24),(79,10,22),(80,10,6),(81,11,23),(82,12,22),(83,13,24);
/*!40000 ALTER TABLE `lams_user_role` ENABLE KEYS */;
UNLOCK TABLES;

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
  `type` varchar(10) DEFAULT NULL COMMENT '资产类型',
  `brand` varchar(10) DEFAULT NULL COMMENT '品牌',
  `price` double DEFAULT NULL COMMENT '价格',
  `adv` json DEFAULT NULL COMMENT '详细信息',
  `readyDate` timestamp NULL DEFAULT NULL COMMENT '入库时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '资产数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user`
--

LOCK TABLES `lams_user` WRITE;
/*!40000 ALTER TABLE `lams_user` DISABLE KEYS */;
INSERT INTO `lams_user` VALUES (3,'系统管理员','18568887789','admin','$2a$10$ySG2lkvjFHY5O0./CPIE1OI8VJsuKYEzOYzqIa7AJR6sEgSzUFOAm',1,'系统管理员');
/*!40000 ALTER TABLE `lams_user` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'/',NULL,NULL,'所有',NULL,NULL,NULL,NULL,1),(2,'/','/home','Home','资产概况','fa fa-dashboard',NULL,1,1,1),(3,'/','/home','Home','资产管理','fa fa-laptop',NULL,1,1,1),(4,'/','/home','Home','我的','fa fa-address-card-o',NULL,1,1,1),(5,'/','/home','Home','项目管理','fa fa-tasks',NULL,1,1,1),(6,'/','/home','Home','系统管理','fa fa-windows',NULL,1,1,1),(7,'/employee/advanced/**','/asset/dashboard','AssetDashBoard','仪表盘',NULL,NULL,1,2,1),(8,'/employee/basic/**','/order/in','AssetIn','资产入库',NULL,NULL,1,3,1),(9,'/employee/basic/**','/order/out','AssetOut','出库借用',NULL,NULL,1,3,1),(10,'/asset/info/**','/asset/info','AssetInfo','资产信息',NULL,NULL,1,3,1),(11,'/statistics/personnel/**','/asset/repair','AssetRepair','资产报修',NULL,NULL,1,3,1),(12,'/statistics/score/**','/asset/clean','AssetClean','清理报废',NULL,NULL,1,3,1),(13,'/personnel/emp/**','/mine/apply','MyApply','我的申请',NULL,NULL,1,4,1),(14,'/personnel/ec/**','/mine/task','MyTask','我的任务',NULL,NULL,1,4,1),(15,'/personnel/train/**','/mine/out','MyOut','离退流程',NULL,NULL,1,4,1),(16,'/system/basic/**','/sys/basic','SysPermission','权限管理',NULL,NULL,1,6,1),(17,'/system/log/**','/sys/log','SysLog','日志管理',NULL,NULL,1,6,1),(18,'/system/hr/**','/sys/hr','SysUser','用户管理',NULL,NULL,1,6,1);
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
  KEY `rid` (`rid`),
  CONSTRAINT `menu_role_ibfk_1` FOREIGN KEY (`mid`) REFERENCES `menu` (`id`),
  CONSTRAINT `menu_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_role`
--

LOCK TABLES `menu_role` WRITE;
/*!40000 ALTER TABLE `menu_role` DISABLE KEYS */;
INSERT INTO `menu_role` VALUES (301,7,6),(302,8,6),(303,9,6),(304,10,6),(305,11,6),(306,12,6),(307,13,6),(308,14,6),(309,15,6),(310,16,6),(311,17,6),(312,18,6),(313,7,24),(314,8,24),(315,9,24),(316,11,24),(317,13,24),(318,14,24),(319,15,24);
/*!40000 ALTER TABLE `menu_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operate_type_workflow`
--

DROP TABLE IF EXISTS `operate_type_workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operate_type_workflow` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operate_type` int DEFAULT NULL COMMENT '操作类型id',
  `workflow_key` varchar(10) DEFAULT NULL COMMENT '工作流key',
  `creator` varchar(10) DEFAULT NULL COMMENT '创建人',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operate_type_workflow`
--

LOCK TABLES `operate_type_workflow` WRITE;
/*!40000 ALTER TABLE `operate_type_workflow` DISABLE KEYS */;
INSERT INTO `operate_type_workflow` VALUES (1,1,'assetIn','胡宏建','2020-09-24'),(2,2,'assetOut','胡宏建','2020-09-26');
/*!40000 ALTER TABLE `operate_type_workflow` ENABLE KEYS */;
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
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oid` bigint DEFAULT NULL COMMENT '资产id，出库的话是出库的id，入库的话是入库的id',
  `type` varchar(10) DEFAULT NULL COMMENT '资产类型',
  `operator` varchar(10) DEFAULT NULL COMMENT '操作人',
  `operatorMail` varchar(10) DEFAULT NULL COMMENT '操作人邮箱',
  `content` varchar(10) DEFAULT NULL COMMENT '操作内容',
  `operateType` int DEFAULT NULL COMMENT '操作类型',
  `operate` varchar(10) DEFAULT NULL COMMENT '操作名称',
  `operateTime` date DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `record`
--

LOCK TABLES `record` WRITE;
/*!40000 ALTER TABLE `record` DISABLE KEYS */;
/*!40000 ALTER TABLE `record` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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

-- Dump completed on 2020-10-21 15:18:30
