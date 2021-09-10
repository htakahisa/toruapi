# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.35)
# Database: toruapi
# Generation Time: 2021-09-10 08:17:08 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table characters
# ------------------------------------------------------------

LOCK TABLES `characters` WRITE;
/*!40000 ALTER TABLE `characters` DISABLE KEYS */;

INSERT INTO `characters` (`id`, `attack`, `dodge_rate`, `hp`, `name`, `special_ability`, `speed`, `waza1`, `waza2`, `waza3`, `waza4`)
VALUES
	(1,90,0.00,30,'とうやま','TORU',30,'PUNCH','PUNCH','PUNCH','PUNCH'),
	(2,80,0.10,70,'おもこ','NONE',150,'PUNCH','PUNCH','PUNCH','PUNCH'),
	(3,100,0.00,90,'まーやま','NONE',30,'PUNCH','PUNCH','PUNCH','PUNCH'),
	(4,40,0.00,150,'はてな','NONE',90,'PUNCH','PUNCH','PUNCH','PUNCH'),
	(5,100,0.00,60,'エリアス','NONE',70,'PUNCH','PUNCH','PUNCH','PUNCH'),
	(6,105,0.00,170,'VALIOM','NONE',1,'PUNCH','PUNCH','PUNCH','PUNCH'),
	(7,70,0.00,70,'こもお','NONE',70,'PUNCH','PUNCH','PUNCH','PUNCH'),
	(8,20,0.00,250,'くびーる','NONE',15,'PUNCH','PUNCH','PUNCH','PUNCH');

/*!40000 ALTER TABLE `characters` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table waza
# ------------------------------------------------------------

LOCK TABLES `waza` WRITE;
/*!40000 ALTER TABLE `waza` DISABLE KEYS */;

INSERT INTO `waza` (`waza`, `append_effect`, `client_action`, `cp`, `in_action`, `name`, `power`, `priority`, `hit_rate`)
VALUES
	('BRILLIANTCOKTAILS','NONE','ATTACK',5,'IN_ATTACK','ブリリアントカクテル',40,0,1.00),
	('CHANGE','NONE','CHANGE',999,'CHANGE','交代',0,99,0.00),
	('CHARGEBEAM','NONE','ATTACK',1,'IN_ATTACK','チャージビーム',50,0,0.90),
	('INIT_CHANGE','NONE','INIT_CHANGE',999,'CHANGE','交代',0,99,0.00),
	('KICK','NONE','ATTACK',10,'IN_ATTACK','キック',30,0,0.90),
	('KYOUUN','NONE','ATTACK',10,'IN_ATTACK','強運',30,1,1.00),
	('NONE','NONE','NONE',999,'NONE','NONE',0,99,0.00),
	('PUNCH','NONE','ATTACK',10,'IN_ATTACK','パンチ',20,0,1.00);

/*!40000 ALTER TABLE `waza` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
