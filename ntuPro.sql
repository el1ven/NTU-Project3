/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50620
 Source Host           : localhost
 Source Database       : ntuPro

 Target Server Type    : MySQL
 Target Server Version : 50620
 File Encoding         : utf-8

 Date: 12/29/2014 18:25:30 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `Deadline`
-- ----------------------------
DROP TABLE IF EXISTS `Deadline`;
CREATE TABLE `Deadline` (
  `did` int(20) NOT NULL AUTO_INCREMENT COMMENT 'deadline id自增',
  `dtime` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'NULL' COMMENT '时间',
  `description` text COLLATE utf8_unicode_ci NOT NULL COMMENT '相关描述',
  `rgid` int(20) NOT NULL COMMENT 'research grant id',
  PRIMARY KEY (`did`),
  KEY `rgid` (`rgid`),
  CONSTRAINT `rgid` FOREIGN KEY (`rgid`) REFERENCES `ResearchGrant` (`rgid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Records of `Deadline`
-- ----------------------------
BEGIN;
INSERT INTO `Deadline` VALUES ('65', '2014-12-27', '金太郎第一个deadline', '67'), ('66', '2014-12-28', '金太郎第二个deadline', '67'), ('76', '2014-12-29', 'asfdagadg', '63'), ('78', '2014-12-23', '1234341123', '63'), ('93', '2014-12-12', '2134134123', '64'), ('95', '2014-12-13', 'adfaewr', '65'), ('98', '2014-12-08', '123', '63');
COMMIT;

-- ----------------------------
--  Table structure for `FMsetDeadline`
-- ----------------------------
DROP TABLE IF EXISTS `FMsetDeadline`;
CREATE TABLE `FMsetDeadline` (
  `did` int(20) NOT NULL AUTO_INCREMENT COMMENT 'deadline id自增',
  `rgid` int(20) NOT NULL COMMENT 'research grant id',
  `deadline1` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `deadline2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `deadline3` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `uid` int(20) DEFAULT NULL,
  PRIMARY KEY (`did`),
  KEY `rgid` (`rgid`),
  KEY `uid` (`uid`),
  KEY `uid_2` (`uid`),
  KEY `uid_3` (`uid`),
  CONSTRAINT `fkey3` FOREIGN KEY (`rgid`) REFERENCES `ResearchGrant` (`rgid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fkey4` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Table structure for `File`
-- ----------------------------
DROP TABLE IF EXISTS `File`;
CREATE TABLE `File` (
  `fid` int(20) NOT NULL AUTO_INCREMENT,
  `rgid` int(20) NOT NULL,
  `filePath` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `fileName` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '文件名称',
  PRIMARY KEY (`fid`),
  KEY `rgid` (`rgid`),
  KEY `rgid_2` (`rgid`),
  CONSTRAINT `rgid2` FOREIGN KEY (`rgid`) REFERENCES `ResearchGrant` (`rgid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Records of `File`
-- ----------------------------
BEGIN;
INSERT INTO `File` VALUES ('25', '64', '/Users/el1ven/Documents/ntuProject/web/upload/16/download.txt', 'download.txt'), ('28', '67', '/Users/el1ven/Documents/ntuProject/web/upload/16/cv131032.docx', 'cv131032.docx'), ('29', '63', '/Users/el1ven/Documents/ntuProject/web/upload/30/lianghongcheng.doc', 'lianghongcheng.doc'), ('36', '65', '/Users/el1ven/Documents/ntuProject/web/upload/16/detail copy.html', 'detail copy.html'), ('37', '65', '/Users/el1ven/Documents/ntuProject/web/upload/16/detail copy.html', 'detail copy.html'), ('38', '65', '/Users/el1ven/Documents/ntuProject/web/upload/16/detail copy.html', 'detail copy.html');
COMMIT;

-- ----------------------------
--  Table structure for `ResearchGrant`
-- ----------------------------
DROP TABLE IF EXISTS `ResearchGrant`;
CREATE TABLE `ResearchGrant` (
  `rgid` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `status` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '是否过期',
  `title` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT '标题',
  `series` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT '属于哪个系列',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT '相关描述',
  `contact` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'rso contact 联系人',
  `present` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`rgid`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Records of `ResearchGrant`
-- ----------------------------
BEGIN;
INSERT INTO `ResearchGrant` VALUES ('63', 'fresh', '计量经济学3', '金融&经济3', '33333', '3333@gmail.com', '2014-12-25 15:40:31'), ('64', 'fresh', '风险管理', '金融&经济', 'aaa', '445009754@qq.com', '2014-12-25 15:47:08'), ('65', 'fresh', '植物的开花与结果', '生物', '哈哈哈', '445009754@qq.com', '2014-12-27 09:41:43'), ('67', 'outdate', '金太郎的幸福生活', '戏剧', 'hahah', '445009754@qq.com', '2014-12-27 13:07:01');
COMMIT;

-- ----------------------------
--  Table structure for `School`
-- ----------------------------
DROP TABLE IF EXISTS `School`;
CREATE TABLE `School` (
  `sid` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '学院名称',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Records of `School`
-- ----------------------------
BEGIN;
INSERT INTO `School` VALUES ('1', 'None'), ('2', 'SCE'), ('3', 'NBS'), ('4', 'EEE'), ('5', 'CEE'), ('6', 'SBC'), ('7', 'WKW'), ('8', 'MAE');
COMMIT;

-- ----------------------------
--  Table structure for `UmanageRG`
-- ----------------------------
DROP TABLE IF EXISTS `UmanageRG`;
CREATE TABLE `UmanageRG` (
  `mid` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id 自增',
  `uid` int(20) NOT NULL COMMENT 'user id',
  `rgid` int(20) NOT NULL COMMENT 'ResearchGrant id',
  PRIMARY KEY (`mid`),
  KEY `uid` (`uid`),
  KEY `rgid` (`rgid`),
  KEY `uid_2` (`uid`),
  KEY `rgid_2` (`rgid`),
  KEY `uid_3` (`uid`),
  KEY `rgid_3` (`rgid`),
  CONSTRAINT `fkey1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fkey2` FOREIGN KEY (`rgid`) REFERENCES `ResearchGrant` (`rgid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Records of `UmanageRG`
-- ----------------------------
BEGIN;
INSERT INTO `UmanageRG` VALUES ('36', '30', '63'), ('37', '16', '64'), ('38', '16', '65'), ('40', '16', '67');
COMMIT;

-- ----------------------------
--  Table structure for `UrelateS`
-- ----------------------------
DROP TABLE IF EXISTS `UrelateS`;
CREATE TABLE `UrelateS` (
  `rid` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `uid` int(20) NOT NULL COMMENT 'user id',
  `sid` int(20) NOT NULL COMMENT 'school id',
  PRIMARY KEY (`rid`),
  KEY `uid` (`uid`),
  KEY `sid` (`sid`),
  CONSTRAINT `rfkey1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rfkey2` FOREIGN KEY (`sid`) REFERENCES `School` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='user-school relationship';

-- ----------------------------
--  Records of `UrelateS`
-- ----------------------------
BEGIN;
INSERT INTO `UrelateS` VALUES ('9', '15', '1'), ('10', '15', '2'), ('11', '16', '3'), ('12', '17', '4'), ('13', '18', '5'), ('14', '16', '2'), ('15', '16', '1'), ('16', '30', '2'), ('17', '30', '3'), ('18', '30', '4');
COMMIT;

-- ----------------------------
--  Table structure for `User`
-- ----------------------------
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `uid` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `type` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '用于标识用户类型',
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户密码',
  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户邮箱',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Records of `User`
-- ----------------------------
BEGIN;
INSERT INTO `User` VALUES ('15', 'FM', 'haha1', 'asdasd', '445009754@qq.com'), ('16', 'RSO', 'haha2', 'asdasd', '445009754@qq.com'), ('17', 'SRO', 'haha3', 'asdasd', '445009754@qq.com'), ('18', 'FM', 'haha4', 'asdasd', '445009754@qq.com'), ('30', 'RSO', 'haha5', 'asdasd', '445009754@qq.com');
COMMIT;

-- ----------------------------
--  Table structure for `UsetT`
-- ----------------------------
DROP TABLE IF EXISTS `UsetT`;
CREATE TABLE `UsetT` (
  `sid` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id 自增',
  `uid` int(20) NOT NULL COMMENT 'userid',
  `time` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'time for alert',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Table structure for `UsubscribeRG`
-- ----------------------------
DROP TABLE IF EXISTS `UsubscribeRG`;
CREATE TABLE `UsubscribeRG` (
  `sid` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id 自增',
  `uid` int(20) NOT NULL COMMENT 'user id',
  `rgid` int(20) NOT NULL COMMENT 'research grant id',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Records of `UsubscribeRG`
-- ----------------------------
BEGIN;
INSERT INTO `UsubscribeRG` VALUES ('9', '15', '61'), ('10', '18', '61'), ('12', '15', '64'), ('13', '18', '63');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
