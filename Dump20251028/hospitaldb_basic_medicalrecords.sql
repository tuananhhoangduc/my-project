-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hospitaldb_basic
-- ------------------------------------------------------
-- Server version	9.4.0

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
-- Table structure for table `medicalrecords`
--

DROP TABLE IF EXISTS `medicalrecords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicalrecords` (
  `RecordID` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PatientID` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DoctorID` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `RoomID` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AdmissionDate` datetime NOT NULL,
  `DischargeDate` datetime DEFAULT NULL,
  `Diagnosis` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `TreatmentPlan` text COLLATE utf8mb4_unicode_ci,
  `RecordStatus` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'Đang điều trị',
  PRIMARY KEY (`RecordID`),
  KEY `FK_Record_Patient` (`PatientID`),
  KEY `FK_Record_Doctor` (`DoctorID`),
  KEY `FK_Record_Room` (`RoomID`),
  CONSTRAINT `FK_Record_Doctor` FOREIGN KEY (`DoctorID`) REFERENCES `doctors` (`DoctorID`),
  CONSTRAINT `FK_Record_Patient` FOREIGN KEY (`PatientID`) REFERENCES `patients` (`PatientID`),
  CONSTRAINT `FK_Record_Room` FOREIGN KEY (`RoomID`) REFERENCES `rooms` (`RoomID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicalrecords`
--

LOCK TABLES `medicalrecords` WRITE;
/*!40000 ALTER TABLE `medicalrecords` DISABLE KEYS */;
INSERT INTO `medicalrecords` VALUES ('BA001','BN001','BS001','P301','2025-10-20 08:30:00','2025-10-27 19:46:20','Rối loạn nhịp tim','Nghỉ ngơi, uống thuốc theo đơn (A, B), theo dõi 24h.','Đã xuất viện'),('BA002','BN002','BS003','P101','2025-10-23 14:00:00','2025-10-27 21:09:22','Gãy xương cẳng tay trái','Nẹp cố định, dùng thuốc giảm đau (C, D). Cần phẫu thuật nẹp vít.','Đã xuất viện'),('BA003','BN003','BS001','P101','2025-10-27 11:08:40','2025-10-28 21:38:13','viêm xoang','Mổ','Đã xuất viện');
/*!40000 ALTER TABLE `medicalrecords` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-28 21:44:58
