-- --------------------------------------------------------
-- ホスト:                          127.0.0.1
-- サーバーのバージョン:                   5.7.35 - MySQL Community Server (GPL)
-- サーバー OS:                      Linux
-- HeidiSQL バージョン:               11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- テーブル toruapi.characters: ~8 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `characters` DISABLE KEYS */;
INSERT INTO `characters` (`id`, `attack`, `dodge_rate`, `hp`, `name`, `special_ability`, `speed`, `waza1`, `waza2`, `waza3`, `waza4`) VALUES
	(1, 90, 0.00, 30, 'とうやま', 'TORU', 91, 'PUNCH', 'PUNCH', 'PUNCH', 'PUNCH'),
	(2, 80, 0.10, 140, 'おもこ', 'OMOKO', 150, 'PUNCH', 'PUNCH', 'PUNCH', 'PUNCH'),
	(3, 100, 0.00, 180, 'まーやま', 'NONE', 60, 'PUNCH', 'PUNCH', 'PUNCH', 'PUNCH'),
	(4, 40, 0.00, 300, 'はてな', 'NONE', 90, 'PUNCH', 'PUNCH', 'PUNCH', 'PUNCH'),
	(5, 100, 0.00, 120, 'エリアス', 'NONE', 70, 'PUNCH', 'PUNCH', 'PUNCH', 'PUNCH'),
	(6, 105, 0.00, 340, 'VALIOM', 'TORU', 50, 'PUNCH', 'PUNCH', 'PUNCH', 'PUNCH'),
	(7, 70, 0.00, 140, 'こもお', 'NONE', 70, 'PUNCH', 'PUNCH', 'PUNCH', 'PUNCH'),
	(8, 20, 0.00, 500, 'くびーる', 'NONE', 55, 'PUNCH', 'PUNCH', 'PUNCH', 'PUNCH');
/*!40000 ALTER TABLE `characters` ENABLE KEYS */;

-- テーブル toruapi.waza: ~11 rows (約) のデータをダンプしています
/*!40000 ALTER TABLE `waza` DISABLE KEYS */;
INSERT INTO `waza` (`waza`, `append_effect`, `append_effect_rate`, `client_action`, `cp`, `hit_rate`, `in_action`, `name`, `power`, `priority`) VALUES
	('BRILLIANTCOKTAILS', 'NONE', 0.00, 'ATTACK', 5, 1.00, 'IN_ATTACK', 'ブリリアントカクテル', 40, 0),
	('CHANGE', 'NONE', 0.00, 'CHANGE', 999, 0.00, 'CHANGE', '交代', 0, 99),
	('CHARGEBEAM', 'NONE', 0.00, 'ATTACK', 1, 0.90, 'IN_ATTACK', 'チャージビーム', 50, 0),
	('FIRE', 'YAKEDO', 0.85, 'EFFECT', 10, 0.80, 'IN_ATTACK', 'ほのお', 20, 0),
	('GIVE_UP', 'NONE', 0.00, 'GIVE_UP', 999, 0.00, 'IN_THE_BATTLE', '降参', 0, 99),
	('HAKAI', 'NONE', 0.00, 'ATTACK', 5, 30.00, 'IN_ATTACK', '破壊', 10000, 0),
	('INIT_CHANGE', 'NONE', 0.00, 'INIT_CHANGE', 999, 0.00, 'CHANGE', '交代', 0, 99),
	('KICK', 'NONE', 0.00, 'ATTACK', 10, 0.90, 'IN_ATTACK', 'キック', 30, 0),
	('KYOUUN', 'NONE', 0.00, 'ATTACK', 10, 1.00, 'IN_ATTACK', '強運', 30, 1),
	('NONE', 'NONE', 0.00, 'NONE', 999, 0.00, 'NONE', 'NONE', 0, 99),
	('OBENTO', 'NONE', 0.00, 'HEALING', 10, 1.00, 'IN_ATTACK', 'お弁当', 50, 0),
	('PUNCH', 'NONE', 0.00, 'ATTACK', 10, 1.00, 'IN_ATTACK', 'パンチ', 20, 0);
/*!40000 ALTER TABLE `waza` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
