CREATE DATABASE  IF NOT EXISTS `lanka_courier_management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `lanka_courier_management`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: lanka_courier_management
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `branches`
--

DROP TABLE IF EXISTS `branches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branches` (
  `branch_id` int NOT NULL AUTO_INCREMENT,
  `b_name` varchar(255) NOT NULL,
  `b_type` varchar(50) NOT NULL,
  `district` varchar(100) NOT NULL,
  `province` varchar(100) NOT NULL,
  `postal_code` varchar(10) NOT NULL,
  `tel_no` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`branch_id`),
  UNIQUE KEY `b_name` (`b_name`),
  UNIQUE KEY `postal_code` (`postal_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branches`
--

LOCK TABLES `branches` WRITE;
/*!40000 ALTER TABLE `branches` DISABLE KEYS */;
INSERT INTO `branches` VALUES (1,'Colombo GPO','General Post Office','Colombo','Western','00100','0117564789'),(2,'Kandy GPO','General Post Office','Kandy','Central','20000',NULL),(3,'Galle GPO','General Post Office','Galle','Southern','80000',NULL),(4,'Jaffna GPO','General Post Office','Jaffna','Northern','40000',NULL),(5,'Matara GPO','General Post Office','Matara','Southern','81000',NULL);
/*!40000 ALTER TABLE `branches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_mobile_numbers`
--

DROP TABLE IF EXISTS `customer_mobile_numbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_mobile_numbers` (
  `mobile_no_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `mobile_no` varchar(15) NOT NULL,
  PRIMARY KEY (`mobile_no_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `customer_mobile_numbers_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_mobile_numbers`
--

LOCK TABLES `customer_mobile_numbers` WRITE;
/*!40000 ALTER TABLE `customer_mobile_numbers` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_mobile_numbers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `customer_type` varchar(50) NOT NULL,
  `business_name` varchar(255) DEFAULT NULL,
  `business_reg_no` varchar(100) DEFAULT NULL,
  `contact_person_name` varchar(255) NOT NULL,
  `contact_person_nic` varchar(20) NOT NULL,
  `address` text NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `account_status` varchar(50) NOT NULL DEFAULT 'Active',
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `contact_person_nic` (`contact_person_nic`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Individual',NULL,NULL,'Test Sender','900000000V','No.1, Test St, Colombo','sender@example.com','sender1',NULL,'Active'),(2,'Individual',NULL,NULL,'Test Receiver','900000001V','No.2, Test Ave, Kandy','receiver@example.com','receiver1',NULL,'Active'),(3,'business','Theepan Mobile','1524','Theepan','199845671564','Vavuniya','Theepan098@gmail.com','Theepan','$2a$10$RkstIQAn8VXfxZjP9o9bwep6driFQ1qmDAHwjTLMCp8HkeSyHe3O2','Active'),(8,'Individual','','','Gagana','200125467895','0761151703','Gagana07@gmai.com','Gagana','72a4e81e5fd6d96b8a9ec2d0225ec0887c6053b4666a3d37d45dc1eb5984a4e9','active');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `employee_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` varchar(50) NOT NULL,
  `mobile_no` varchar(15) DEFAULT NULL,
  `branch_id` int DEFAULT NULL,
  `photo_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `mobile_no` (`mobile_no`),
  KEY `branch_id` (`branch_id`),
  CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'Admin','User','admin','$2a$10$0QomNb4g6oGGmUfApedJc.5p8K4r2Wu/AoAvAysYHIECrzLbexuae','Admin','0770000000',1,NULL),(2,'Magil','Maran','Magil','$2a$10$XlU2rkPYTxngsWuwP2UEm.dNn23PKxM9n8cz7tFbZ./6MgDT89Lo.','Clerk','0761157896',4,NULL),(3,'Pathum','Manusha','Pathum','$2a$10$uv3mBzGaMuxY9tYfE/9u2eFs419pH0i52rhxPkM68ITT/Mj29qcU.','Driver','0774867985',3,NULL);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parcels`
--

DROP TABLE IF EXISTS `parcels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parcels` (
  `parcel_id` int NOT NULL AUTO_INCREMENT,
  `tracking_id` varchar(20) NOT NULL,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `origin_branch_id` int NOT NULL,
  `destination_branch_id` int DEFAULT NULL,
  `description` text,
  `weight_kg` decimal(10,3) NOT NULL,
  `price_lkr` decimal(10,2) NOT NULL,
  `status` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_cod` tinyint(1) NOT NULL DEFAULT '0',
  `cod_amount_lkr` decimal(10,2) DEFAULT NULL,
  `service_charge_lkr` decimal(10,2) DEFAULT NULL,
  `mo_commission_lkr` decimal(10,2) DEFAULT NULL,
  `cod_payment_status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`parcel_id`),
  UNIQUE KEY `tracking_id` (`tracking_id`),
  KEY `sender_id` (`sender_id`),
  KEY `receiver_id` (`receiver_id`),
  KEY `origin_branch_id` (`origin_branch_id`),
  KEY `destination_branch_id` (`destination_branch_id`),
  CONSTRAINT `parcels_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `customers` (`customer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `parcels_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `customers` (`customer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `parcels_ibfk_3` FOREIGN KEY (`origin_branch_id`) REFERENCES `branches` (`branch_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `parcels_ibfk_4` FOREIGN KEY (`destination_branch_id`) REFERENCES `branches` (`branch_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parcels`
--

LOCK TABLES `parcels` WRITE;
/*!40000 ALTER TABLE `parcels` DISABLE KEYS */;
INSERT INTO `parcels` VALUES (1,'01',2,1,1,3,NULL,10.000,800.00,'Registered','2025-12-17 00:14:58',0,NULL,90.00,0.00,NULL);
/*!40000 ALTER TABLE `parcels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rates`
--

DROP TABLE IF EXISTS `rates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rates` (
  `rate_id` int NOT NULL AUTO_INCREMENT,
  `min_weight_kg` decimal(10,3) NOT NULL,
  `max_weight_kg` decimal(10,3) NOT NULL,
  `price_lkr` decimal(10,2) NOT NULL,
  PRIMARY KEY (`rate_id`),
  CONSTRAINT `rates_chk_1` CHECK ((`min_weight_kg` <= `max_weight_kg`))
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rates`
--

LOCK TABLES `rates` WRITE;
/*!40000 ALTER TABLE `rates` DISABLE KEYS */;
INSERT INTO `rates` VALUES (1,0.000,0.250,200.00),(2,0.250,0.500,250.00),(3,0.500,1.000,350.00),(4,1.000,2.000,400.00),(5,2.000,3.000,450.00),(6,3.000,4.000,500.00),(7,4.000,5.000,550.00),(8,5.000,6.000,600.00),(9,6.000,7.000,650.00),(10,7.000,8.000,700.00),(11,8.000,9.000,750.00),(12,9.000,10.000,800.00),(13,10.000,15.000,850.00),(14,15.000,20.000,1100.00),(15,20.000,25.000,1600.00),(16,25.000,30.000,2100.00),(17,30.000,35.000,2600.00),(18,35.000,40.000,3100.00);
/*!40000 ALTER TABLE `rates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tracking_history`
--

DROP TABLE IF EXISTS `tracking_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tracking_history` (
  `history_id` int NOT NULL AUTO_INCREMENT,
  `parcel_id` int NOT NULL,
  `branch_id` int NOT NULL,
  `employee_id` int NOT NULL,
  `status` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`history_id`),
  KEY `parcel_id` (`parcel_id`),
  KEY `branch_id` (`branch_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `tracking_history_ibfk_1` FOREIGN KEY (`parcel_id`) REFERENCES `parcels` (`parcel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tracking_history_ibfk_2` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `tracking_history_ibfk_3` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tracking_history`
--

LOCK TABLES `tracking_history` WRITE;
/*!40000 ALTER TABLE `tracking_history` DISABLE KEYS */;
INSERT INTO `tracking_history` VALUES (1,1,4,2,'Processing','2025-12-17 00:16:00'),(2,1,1,1,'IN TRANSIT','2025-12-17 00:29:02'),(3,1,1,1,'CANCELLED','2025-12-17 00:49:09'),(4,1,1,1,'RETURNED','2025-12-17 03:00:56'),(5,1,1,1,'IN TRANSIT','2025-12-17 03:25:56'),(6,1,1,3,'IN TRANSIT','2025-12-17 03:29:15');
/*!40000 ALTER TABLE `tracking_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-17 14:47:26
