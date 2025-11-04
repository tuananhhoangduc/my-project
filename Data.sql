CREATE DATABASE  IF NOT EXISTS `hospitaldb_basic` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hospitaldb_basic`;
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
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `AccountID` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Role` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`AccountID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES ('AD001','admin','admin123','Admin'),('DR001','bs.an','an.tim mach!1','Doctor'),('DR002','bs.binh','binh.noi@22','Doctor'),('DR003','bs.cuong','cuong_chanthuong','Doctor'),('DR004','bs.dung','dung_sanphu*','Doctor'),('DR005','bs.giang','giangnhi@123','Doctor'),('DR006','bs.hoa','hoa.dalieu#','Doctor'),('DR007','bs.huong','huong.mat_2025','Doctor'),('DR008','bs.khanh','khanh.tmh!','Doctor'),('DR009','bs.linh','linh.ungbuou','Doctor'),('DR010','bs.long','long_thankinh','Doctor'),('DR011','bs.minh','minh.tim mach2','Doctor'),('DR012','bs.ngoc','ngoc.noi@456','Doctor'),('DR013','bs.phuong','phuong.nhi_789','Doctor'),('DR014','bs.quan','quan.ct@c3','Doctor'),('DR015','bs.son','son.dalieu!','Doctor'),('DR016','bs.tam','tam_sanphu_khoa','Doctor'),('DR017','bs.thanh','thanh.mat@bv','Doctor'),('DR018','bs.trang','trang.tmh_01','Doctor'),('DR019','bs.tuan','tuan_ub@2024','Doctor'),('DR020','bs.yen','yen.thankinh#55','Doctor');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors` (
  `DoctorID` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `FullName` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Gender` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `PhoneNumber` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Specialty` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `AccountID` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`DoctorID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES ('BS001','Nguyễn Văn An','Nam','0905123456','Tim mạch','DR001'),('BS002','Trần Thị Bình','Nữ','0918765432','Nội tổng hợp','DR002'),('BS003','Lê Văn Cường','Nam','0988111222','Ngoại chấn thương','DR003'),('BS004','Phạm Thị Dung','Nữ','0977333444','Sản phụ khoa','DR004'),('BS005','Hoàng Văn Giang','Nam','0356555666','Nhi','DR005'),('BS006','Đỗ Thị Hoa','Nữ','0367123789','Da liễu','DR006'),('BS007','Vũ Thị Hương','Nữ','0868456123','Mắt','DR007'),('BS008','Đặng Minh Khánh','Nam','0935789456','Tai mũi họng','DR008'),('BS009','Bùi Thùy Linh','Nữ','0779654321','Ung bướu','DR009'),('BS010','Ngô Gia Long','Nam','0944111333','Thần kinh','DR010'),('BS011','Phan Ngọc Minh','Nam','0905234567','Tim mạch','DR011'),('BS012','Trịnh Thị Ngọc','Nữ','0917876543','Nội tổng hợp','DR012'),('BS013','Lý Mai Phương','Nữ','0986222333','Nhi','DR013'),('BS014','Hồ Hữu Quân','Nam','0974444555','Ngoại chấn thương','DR014'),('BS015','Dương Thái Sơn','Nam','0355666777','Da liễu','DR015'),('BS016','Mai Anh Tâm','Nữ','0366123456','Sản phụ khoa','DR016'),('BS017','Nguyễn Đức Thành','Nam','0869765123','Mắt','DR017'),('BS018','Lê Thu Trang','Nữ','0933456789','Tai mũi họng','DR018'),('BS019','Võ Anh Tuấn','Nam','0778123654','Ung bướu','DR019'),('BS020','Trần Hải Yến','Nữ','0945987321','Thần kinh','DR020');
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

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
INSERT INTO `medicalrecords` VALUES ('BA001','BN001','BS001','P301','2025-10-20 08:30:00',NULL,'Rối loạn nhịp tim','Nghỉ ngơi, uống thuốc theo đơn, theo dõi 24h.','Đang điều trị'),('BA002','BN002','BS003','P101','2025-10-23 14:00:00',NULL,'Gãy xương cẳng tay trái','Nẹp cố định, dùng thuốc giảm đau. Cần phẫu thuật.','Đang điều trị'),('BA003','BN003','BS005','P101','2025-10-23 15:00:00',NULL,'Viêm phổi nặng','Thở oxy, kháng sinh tĩnh mạch, hạ sốt.','Đang điều trị'),('BA004','BN004','BS004','P302','2025-10-24 09:00:00',NULL,'Dọa sinh non','Nằm nghỉ tuyệt đối, dùng thuốc giảm co.','Đang điều trị'),('BA005','BN005','BS007','P201','2025-10-24 10:15:00',NULL,'Viêm kết mạc cấp','Cách ly, nhỏ thuốc kháng sinh, vệ sinh mắt.','Đang điều trị'),('BA006','BN006','BS009','P201','2025-10-24 11:00:00',NULL,'Nghi ngờ U vú','Làm sinh thiết, chụp X-quang, xét nghiệm máu.','Đang điều trị'),('BA007','BN007','BS011','P303','2025-10-25 08:00:00',NULL,'Nhồi máu cơ tim cấp','Can thiệp mạch vành, dùng thuốc chống đông.','Đang điều trị'),('BA008','BN008','BS014','P102','2025-10-25 09:30:00',NULL,'Chấn thương đầu gối','Chụp MRI, bất động khớp, dùng thuốc kháng viêm.','Đang điều trị'),('BA009','BN009','BS010','P202','2025-10-25 10:00:00',NULL,'Đau nửa đầu Migraine','Truyền dịch giảm đau, thuốc đặc trị, nghỉ ngơi.','Đang điều trị'),('BA010','BN010','BS006','P101','2025-10-25 11:00:00',NULL,'Viêm da cơ địa bội nhiễm','Kháng sinh, kem bôi Corticoid, thuốc kháng Histamin.','Đang điều trị'),('BA011','BN011','BS002',NULL,'2025-10-25 08:10:00',NULL,'Cảm cúm thông thường','Thuốc hạ sốt, nghỉ ngơi, uống nhiều nước.','Đang điều trị'),('BA012','BN012','BS008',NULL,'2025-10-25 08:30:00',NULL,'Viêm họng hạt','Kháng sinh, nước súc miệng, xịt họng.','Đang điều trị'),('BA013','BN013','BS013',NULL,'2025-10-25 09:15:00',NULL,'Sốt phát ban (Nghi sởi)','Hạ sốt, vitamin A, cách ly tại nhà.','Đang điều trị'),('BA014','BN014','BS016',NULL,'2025-10-25 09:45:00',NULL,'Khám thai định kỳ','Siêu âm, xét nghiệm nước tiểu, bổ sung sắt.','Đang điều trị'),('BA015','BN015','BS015',NULL,'2025-10-25 10:30:00',NULL,'Nấm da','Thuốc bôi kháng nấm, giữ vệ sinh khô ráo.','Đang điều trị'),('BA016','BN016','BS017',NULL,'2025-10-25 11:20:00',NULL,'Tật khúc xạ (Cận thị)','Đo mắt, cắt kính mới.','Đang điều trị'),('BA017','BN017','BS013',NULL,'2025-10-25 13:30:00',NULL,'Tiêu chảy cấp','Bù điện giải (Oresol), men vi sinh.','Đang điều trị'),('BA018','BN018','BS001',NULL,'2025-10-25 14:00:00',NULL,'Tăng huyết áp vô căn','Đổi thuốc, điều chỉnh chế độ ăn.','Đang điều trị'),('BA019','BN019','BS019',NULL,'2025-10-25 14:30:00',NULL,'Tái khám sau phẫu thuật','Vết mổ khô, ổn định, cắt chỉ.','Đang điều trị'),('BA020','BN020','BS020',NULL,'2025-10-25 15:00:00',NULL,'Rối loạn tiền đình','Thuốc tuần hoàn não, tập luyện phục hồi chức năng.','Đang điều trị'),('BA021','BN021','BS001','P304','2025-10-01 10:00:00','2025-10-05 10:00:00','Suy tim độ 2','Điều trị nội khoa ổn định.','Đã xuất viện'),('BA022','BN022','BS004','P304','2025-10-06 11:00:00','2025-10-10 11:00:00','Sinh thường','Mẹ tròn con vuông.','Đã xuất viện'),('BA023','BN023','BS003','P103','2025-10-10 12:00:00','2025-10-15 12:00:00','Trật khớp vai','Nắn chỉnh, băng cố định.','Đã xuất viện'),('BA024','BN024','BS005','P103','2025-10-11 13:00:00','2025-10-15 13:00:00','Viêm phế quản','Điều trị kháng sinh 5 ngày.','Đã xuất viện'),('BA025','BN025','BS002',NULL,'2025-10-15 08:00:00','2025-10-15 10:00:00','Đau dạ dày','Nội soi, kê đơn thuốc 1 tháng.','Đã xuất viện'),('BA026','BN026','BS006',NULL,'2025-10-16 09:00:00','2025-10-16 11:00:00','Mụn trứng cá','Lấy nhân mụn, kê thuốc bôi và uống.','Đã xuất viện'),('BA027','BN027','BS008','P203','2025-10-17 10:00:00','2025-10-20 10:00:00','Viêm xoang cấp','Rửa xoang, kháng sinh 3 ngày.','Đã xuất viện'),('BA028','BN028','BS010',NULL,'2025-10-18 11:00:00','2025-10-18 12:00:00','Đau đầu căng cơ','Thuốc giảm đau, nghỉ ngơi.','Đã xuất viện'),('BA029','BN029','BS012','P203','2025-10-19 14:00:00','2025-10-22 14:00:00','Sốt xuất huyết','Truyền dịch, theo dõi tiểu cầu.','Đã xuất viện'),('BA030','BN030','BS015',NULL,'2025-10-20 15:00:00','2025-10-20 16:00:00','Dị ứng thời tiết','Thuốc kháng Histamin.','Đã xuất viện'),('BA031','BN031','BS011','P304','2025-10-15 10:00:00','2025-10-19 10:00:00','Theo dõi tăng huyết áp','Ổn định huyết áp, cho về.','Đã xuất viện'),('BA032','BN032','BS013','P104','2025-10-18 08:00:00','2025-10-21 08:00:00','Viêm ruột thừa','Phẫu thuật nội soi cắt ruột thừa.','Đã xuất viện'),('BA033','BN033','BS014',NULL,'2025-10-20 09:00:00','2025-10-20 10:30:00','Bong gân cổ chân','Chườm đá, băng thun, nghỉ ngơi.','Đã xuất viện'),('BA034','BN034','BS016','P304','2025-10-20 11:00:00','2025-10-23 11:00:00','Viêm phụ khoa','Điều trị thuốc đặt.','Đã xuất viện'),('BA035','BN035','BS017',NULL,'2025-10-21 14:00:00','2025-10-21 15:00:00','Chắp lẹo mắt','Chích chắp lẹo, kê đơn thuốc nhỏ mắt.','Đã xuất viện'),('BA036','BN036','BS005','P104','2025-10-22 10:00:00','2025-10-24 10:00:00','Hen suyễn (cấp)','Xông khí dung, ổn định hô hấp.','Đã xuất viện'),('BA037','BN037','BS002',NULL,'2025-10-22 11:00:00','2025-10-22 12:00:00','Tư vấn sức khỏe','Tư vấn, không dùng thuốc.','Đã xuất viện'),('BA038','BN038','BS009',NULL,'2025-10-23 09:00:00','2025-10-23 11:00:00','Tầm soát ung thư','Kết quả bình thường, hẹn 6 tháng tái khám.','Đã xuất viện'),('BA039','BN039','BS019','P204','2025-10-15 16:00:00','2025-10-22 16:00:00','Hóa trị đợt 1','Hoàn thành hóa trị, theo dõi tại nhà.','Đã xuất viện'),('BA040','BN040','BS020',NULL,'2025-10-24 10:00:00','2025-10-24 11:00:00','Mất ngủ kinh niên','Kê đơn thuốc an thần nhẹ, tư vấn tâm lý.','Đã xuất viện');
/*!40000 ALTER TABLE `medicalrecords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `PatientID` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `FullName` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DateOfBirth` date NOT NULL,
  `Gender` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `PhoneNumber` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`PatientID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES ('BN001','Nguyễn Văn A','1980-01-15','Nam','0987111222','12 Quang Trung, Hà Nội'),('BN002','Trần Thị B','1992-03-22','Nữ','0912345678','45 Lê Lợi, TP.HCM'),('BN003','Lê Văn C','1975-07-30','Nam','0933456789','78 Nguyễn Huệ, Đà Nẵng'),('BN004','Phạm Thị D','2001-11-05','Nữ','0978123456','101 Trần Hưng Đạo, Cần Thơ'),('BN005','Hoàng Văn E','1995-02-18','Nam','0356789123','22 Bà Triệu, Hà Nội'),('BN006','Đỗ Thị F','1988-09-12','Nữ','0868123789','33 Lý Thường Kiệt, TP.HCM'),('BN007','Vũ Văn G','1963-12-25','Nam','0905456123','56 Hai Bà Trưng, Hải Phòng'),('BN008','Đặng Thị H','1999-06-08','Nữ','0913789456','89 Võ Nguyên Giáp, Hà Nội'),('BN009','Bùi Văn I','1982-04-14','Nam','0988654321','21 Ngô Quyền, Huế'),('BN010','Ngô Thị K','1990-08-03','Nữ','0909123456','123 Phạm Văn Đồng, TP.HCM'),('BN011','Phan Văn L','1955-03-10','Nam','0912233445','234 Trường Chinh, Hà Nội'),('BN012','Trịnh Thị M','2005-01-20','Nữ','0934567890','345 Giải Phóng, Hà Nội'),('BN013','Lý Văn N','1998-10-17','Nam','0976543210','456 Lê Duẩn, Đà Nẵng'),('BN014','Hồ Thị O','1987-12-01','Nữ','0869123456','567 Nguyễn Văn Cừ, TP.HCM'),('BN015','Dương Văn P','1979-05-05','Nam','0357890123','678 Hùng Vương, Cần Thơ'),('BN016','Mai Thị Q','1993-11-11','Nữ','0908765123','789 An Dương Vương, TP.HCM'),('BN017','Nguyễn Hữu R','2010-07-07','Nam','0915678345','890 Trần Phú, Hà Nội'),('BN018','Lê Thị S','1968-09-09','Nữ','0981234789','901 Nguyễn Trãi, TP.HCM'),('BN019','Võ Văn T','1996-04-23','Nam','0935456789','112 Võ Thị Sáu, Vũng Tàu'),('BN020','Trần Thị U','1983-02-28','Nữ','0979123654','223 Nguyễn Thị Minh Khai, TP.HCM'),('BN021','Hoàng Văn V','1977-06-19','Nam','0388999111','334 CMT8, TP.HCM'),('BN022','Đặng Thị X','2002-10-30','Nữ','0912888777','445 Nguyễn Oanh, TP.HCM'),('BN023','Bùi Văn Y','1989-01-15','Nam','0903666555','556 Quang Trung, Hà Nội'),('BN024','Ngô Thị Z','1991-08-20','Nữ','0975444333','667 Lê Văn Sỹ, TP.HCM'),('BN025','Phan Văn AA','1970-12-12','Nam','0867222111','778 Hoàng Hoa Thám, Hà Nội'),('BN026','Trịnh Thị BB','1997-05-18','Nữ','0919333222','889 Lạc Long Quân, TP.HCM'),('BN027','Lý Văn CC','1984-02-25','Nam','0984555666','990 Âu Cơ, Hà Nội'),('BN028','Hồ Thị DD','2000-11-03','Nữ','0908777999','101 Nguyễn Kiệm, TP.HCM'),('BN029','Dương Văn EE','1994-07-14','Nam','0399111222','202 Trần Bình Trọng, Cần Thơ'),('BN030','Mai Thị FF','1981-09-01','Nữ','0913222333','303 Võ Văn Tần, TP.HCM'),('BN031','Nguyễn Hữu GG','1976-03-28','Nam','0985333444','404 Lê Thánh Tôn, TP.HCM'),('BN032','Lê Thị HH','2003-10-10','Nữ','0976444555','505 Pastuer, TP.HCM'),('BN033','Võ Văn II','1993-01-05','Nam','0367555666','606 Điện Biên Phủ, Hà Nội'),('BN034','Trần Thị KK','1986-08-15','Nữ','0902666777','707 Lý Chính Thắng, TP.HCM'),('BN035','Hoàng Văn LL','1973-12-07','Nam','0918777888','808 Nam Kỳ Khởi Nghĩa, TP.HCM'),('BN036','Đặng Thị MM','2008-04-20','Nữ','0983888999','909 Thảo Điền, TP.HCM'),('BN037','Bùi Văn NN','1995-11-22','Nam','0974999000','121 Tôn Đức Thắng, Hà Nội'),('BN038','Ngô Thị PP','1980-02-14','Nữ','0358111222','212 Xã Đàn, Hà Nội'),('BN039','Phan Văn QQ','1974-09-03','Nam','0916222333','313 Kim Mã, Hà Nội'),('BN040','Trịnh Thị RR','1992-06-06','Nữ','0989333444','414 Láng Hạ, Hà Nội');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `RoomID` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `RoomNumber` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `RoomType` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Capacity` int DEFAULT '1',
  `CurrentOccupancy` int DEFAULT '0',
  `Status` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'Trống',
  `PricePerDay` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`RoomID`),
  UNIQUE KEY `RoomNumber` (`RoomNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES ('P101','101','Phòng thường',4,3,'Còn chỗ',300000.00),('P102','102','Phòng thường',4,1,'Còn chỗ',300000.00),('P103','103','Phòng thường',4,0,'Trống',300000.00),('P104','104','Phòng thường',4,0,'Trống',300000.00),('P201','201','Phòng thường',4,2,'Còn chỗ',350000.00),('P202','202','Phòng thường',4,1,'Còn chỗ',350000.00),('P203','203','Phòng thường',4,0,'Trống',350000.00),('P204','204','Phòng thường',4,0,'Trống',350000.00),('P301','301','Phòng VIP',1,1,'Đầy',1000000.00),('P302','302','Phòng VIP',1,1,'Đầy',1000000.00),('P303','303','Phòng VIP',1,1,'Đầy',1000000.00),('P304','304','Phòng VIP',1,0,'Trống',1000000.00);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-04 16:42:42
