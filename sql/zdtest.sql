/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : 127.0.0.1:3306
 Source Schema         : zdtest

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 07/11/2018 18:22:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('scheduler', 'c.6.t', 'c.6.tg', '0 0/2 * * * ?', 'Asia/Shanghai');
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('scheduler', 'c.7.t', 'c.7.tg', '0 0/1 * * * ?', 'Asia/Shanghai');
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('scheduler', 'c.6', 'c.6.g', NULL, 'smarttesting.service.job.CaseJob', '1', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('scheduler', 'c.7', 'c.7.g', NULL, 'smarttesting.service.job.CaseJob', '1', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_LOCKS` VALUES ('scheduler', 'STATE_ACCESS');
INSERT INTO `QRTZ_LOCKS` VALUES ('scheduler', 'TRIGGER_ACCESS');
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('scheduler', 'XNMAC-C02W29EVH.local1541585998537', 1541586111475, 7500);
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_TRIGGERS` VALUES ('scheduler', 'c.6.t', 'c.6.tg', 'c.6', 'c.6.g', NULL, 1541586120000, -1, 5, 'WAITING', 'CRON', 1541586001000, 0, NULL, 2, '');
INSERT INTO `QRTZ_TRIGGERS` VALUES ('scheduler', 'c.7.t', 'c.7.tg', 'c.7', 'c.7.g', NULL, 1541586120000, -1, 5, 'WAITING', 'CRON', 1541586084000, 0, NULL, 2, '');
COMMIT;

-- ----------------------------
-- Table structure for zd_case
-- ----------------------------
DROP TABLE IF EXISTS `zd_case`;
CREATE TABLE `zd_case` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) NOT NULL,
  `interface_id` bigint(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `method` varchar(128) NOT NULL,
  `url` varchar(2048) NOT NULL,
  `url_suffix` varchar(2048) NOT NULL,
  `content_type` varchar(256) NOT NULL,
  `request_body` text NOT NULL,
  `result_script` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id_index` (`project_id`),
  KEY `interface_id_index` (`interface_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zd_case
-- ----------------------------
BEGIN;
INSERT INTO `zd_case` VALUES (1, 36, 6, '电话接口1', 'POST', 'https://tcc.taobao.com/cc/json/mobile_tel_segment.htm', '', '', '{\"tel\":\"13888888888\"}', '');
INSERT INTO `zd_case` VALUES (2, 36, 6, '电话接口2', 'POST', 'https://tcc.taobao.com/cc/json/mobile_tel_segment.htm', '', '', '{\"tel\":\"13777777777\"}', '');
INSERT INTO `zd_case` VALUES (3, 36, 6, '电话接口3', 'POST', 'https://tcc.taobao.com/cc/json/mobile_tel_segment.htm', '', '', '{\"tel\":\"133888\"}', '');
INSERT INTO `zd_case` VALUES (4, 36, 5, '成都地址接口', 'POST', 'http://gc.ditu.aliyun.com/geocoding', '', 'application/x-www-form-urlencoded', '{\"a\":\"成都\"}', '');
INSERT INTO `zd_case` VALUES (5, 36, 5, '32', 'POST', 'http://gc.ditu.aliyun.com/geocoding', '', 'application/x-www-form-urlencoded', '34234', '');
INSERT INTO `zd_case` VALUES (6, 37, 10, '测试用例-MAP', 'GET', 'http://127.0.0.1:6060', '/data/test/map.json?d=1', 'application/x-www-form-urlencoded', '{\n\n\n    \"a\": \"1\",\n    \"b\": \"2\",\n    \"c\": 3\n\n}', '');
INSERT INTO `zd_case` VALUES (7, 37, 10, '测试用例-数组', 'GET', 'http://127.0.0.1:6060', '/data/test/vector.json', 'application/x-www-form-urlencoded', '{\n    \"ids\": [1, 2]\n}', '');
INSERT INTO `zd_case` VALUES (8, 37, 10, '测试用例-对象', 'GET', 'http://127.0.0.1:6060', '/data/test/domain.json', 'application/x-www-form-urlencoded', '{\n    \"a\": \"2\",\n    \"b\": \"3\",\n    \"c\": {\n        \"c_a\": 1,\n        \"c_b\": 2\n    }\n}', '');
INSERT INTO `zd_case` VALUES (9, 37, 10, '测试用例-参数', 'GET', 'http://127.0.0.1:6060', '/data/test/parameter.json', 'application/x-www-form-urlencoded', '{\n\n    \"id\": 1,\n    \"name\": \"中文\",\n    \"c\": \"3\",\n    \"d\": \"2\"\n}', '');
INSERT INTO `zd_case` VALUES (10, 37, 10, '测试用例-JSON', 'GET', 'http://127.0.0.1:6060', '/data/test/map_json.json', 'application/json', '{\n\n    \"id\": 1,\n    \"name\": \"中文\",\n    \"c\": \"3\",\n    \"d\": \"2\"\n}', '');
INSERT INTO `zd_case` VALUES (11, 37, 10, '测试用例-对象集合', 'GET', 'http://127.0.0.1:6060', '/data/test/domains.json', 'application/json', '[\n    {\n        \"a\": \"2\",\n        \"b\": \"3\",\n        \"c\": {\n            \"c_a\": 1,\n            \"c_b\": 2\n        }\n},\n\n    {\n        \"a\": \"2\",\n        \"b\": \"3\",\n        \"c\": {\n            \"c_a\": 1,\n            \"c_b\": 2\n        }\n}\n\n]', '');
INSERT INTO `zd_case` VALUES (13, 37, 10, '测试用例-参数-入参为URL', 'GET', 'http://127.0.0.1:6060', '/data/test/domain.json', 'application/x-www-form-urlencoded', 'a=2&b=3&c.a=1&c.b=2', '');
INSERT INTO `zd_case` VALUES (14, 35, 11, '用例1', 'POST', 'https://www.baidu.com', '/s', 'application/x-www-form-urlencoded', 'word=李白', '');
INSERT INTO `zd_case` VALUES (15, 35, 11, '1', 'POST', 'https://www.baidu.com', '/d', 'application/x-www-form-urlencoded', '', '');
INSERT INTO `zd_case` VALUES (16, 35, 13, '42342', 'POST', 'https://www.163.com', '', 'application/x-www-form-urlencoded', '{}', '');
INSERT INTO `zd_case` VALUES (17, 1, 14, '212', 'POST', '333', '', 'application/x-www-form-urlencoded', '33', '');
INSERT INTO `zd_case` VALUES (18, 49, 15, 'a=2&b=3&c.a=1&c.b=2', 'GET', 'http://127.0.0.1:6060/data/test/domain.json', '', 'application/x-www-form-urlencoded', 'a=2&b=3&c.a=1&c.b=1', '#code == 200 && #object.data.a == \"2\"');
INSERT INTO `zd_case` VALUES (19, 49, 16, '1', 'POST', 'http://127.0.0.1', '/1', 'application/json', '1', '');
INSERT INTO `zd_case` VALUES (20, 49, 16, '新用例', 'POST', 'http://127.0.0.1', '', 'application/x-www-form-urlencoded', '', '');
INSERT INTO `zd_case` VALUES (21, 49, 16, '新用例1', 'POST', 'http://127.0.0.1', '', 'application/x-www-form-urlencoded', '', '');
INSERT INTO `zd_case` VALUES (22, 49, 16, '新用例3', 'POST', 'http://127.0.0.1', '', 'application/x-www-form-urlencoded', '', '');
INSERT INTO `zd_case` VALUES (23, 49, 15, '新用例4', 'GET', 'http://127.0.0.1:6060/data/test/domain.json', '', 'application/x-www-form-urlencoded', '', '');
INSERT INTO `zd_case` VALUES (24, 49, 16, '新用例5', 'POST', 'http://127.0.0.1', '', 'application/x-www-form-urlencoded', '', '');
INSERT INTO `zd_case` VALUES (25, 49, 16, '新用例6', 'POST', 'http://127.0.0.1', '', 'application/x-www-form-urlencoded', '', '');
INSERT INTO `zd_case` VALUES (26, 49, 15, '新用例7', 'GET', 'http://127.0.0.1:6060/data/test/domain.json', '', 'application/x-www-form-urlencoded', '{\n    \"a\": 1,\n    \"b\": 2\n}', '#code==200 && #object.data.a==1');
INSERT INTO `zd_case` VALUES (27, 49, 16, '新用例8', 'POST', 'http://127.0.0.1', '', 'application/x-www-form-urlencoded', '', '');
INSERT INTO `zd_case` VALUES (28, 49, 16, '新用例9', 'POST', 'http://127.0.0.1', '', 'application/x-www-form-urlencoded', '', '');
INSERT INTO `zd_case` VALUES (29, 50, 19, '新用例', 'POST', 'http://www.163.com', '', 'application/x-www-form-urlencoded', '', '');
COMMIT;

-- ----------------------------
-- Table structure for zd_interface
-- ----------------------------
DROP TABLE IF EXISTS `zd_interface`;
CREATE TABLE `zd_interface` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `type` varchar(128) NOT NULL,
  `url` varchar(2048) NOT NULL,
  `method` varchar(128) NOT NULL,
  `request_header` text NOT NULL,
  `response_charset` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id_index` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zd_interface
-- ----------------------------
BEGIN;
INSERT INTO `zd_interface` VALUES (1, 36, '接口1、、、', 'http', 'http://www.baidu.com', 'POST', '{\n\n大萨达\n\n\n}', '');
INSERT INTO `zd_interface` VALUES (2, 36, '接口111', 'http', 'q11', 'POST', '{\ncookie:\"o2Control=webp|lastvisit=19; ipLoc-djd=22-1930-50947-52198.138145758; shshshfpb=1499b85cc38694025b273d7491f0c51d602525ff7e2bfd4575af2c0267; shshshfpa=ecb7e670-0594-6b3c-f235-7fffe5e97d1c-1533022882; pinId=EGXorpkh1-7XgZHD7j7zXA; pin=906447521; unick=%E6%89%AD%E6%9B%B2%E7%9A%84%E7%8C%AA%E6%89%92; _tp=GL8jOD2VSorprrIrU941hw%3D%3D; _pst=906447521; unpl=V2_ZzNtbUsDFxN1CkdRKBELAmICQA5KBxFBIFxEAHtNX1ZvBBYOclRCFXwUR1xnGloUZgsZWEFcQBJFCHZXfBpaAmEBFl5yBBNNIEwEACtaDlwJARFZRlZDFXALQVRLKV8FVwMTbUJTQBF1D0JUfxBeBGUEG19CX0MWdQt2ZHwpbDVgBRdZRFVzFEUJdhYvRVQAZQUbXg9XRxZxCEFQex1VB2YBFVRAV0sVdghFZHopXw%3d%3d; _AIRLINE_VALUE_=\"s8m2vCzW2MfsLDIwMTgtMTAtMDUsMSxPVw==\"; __jdv=122270672|weixin|t_1000072672_17053_001|weixin|not set|1538275189370; PCSYCityID=1930; user-key=8c810c8c-a3ef-45e7-a86e-8796e311c37f; __jdu=1525165158303683191212; ceshi3.com=201; sso.jd.com=7b049f4061df4887b20e79f43ebce1f8; TrackID=1EK2BBb5DqUbA8P9c1B7VePeWsBkw-8PvkQJsAFMYvSr31TvfHLX8wcV-j9zelOqbkwcCyArWJ8ssfz9ZVKkZrL69B-Q7t2LVcaBQtmn6VjXj9CFcxDbvszP6x3IF4b8o; __jdc=122270672; ipLocation=%u56DB%u5DDD; areaId=22; cn=61; shshshfp=6c844c74bc417ce9dbb74417afe51095; thor=21AC8712BC8C31589C13F2F65A602680CAA060A2DCB623A8D68B108C0D67E5A1B6301AFC4E79E6D859A1E56041F6627865DDE6B0A0C2A2BCAE0583D56C90B3EBF49D123F57770047D87C53937A83422A564D175326526101D09167075123601D59D7AB81DE410D631D726B5F9D43D6978495C5A85FE529B7841009E23E3EBF54A8A0A45491F23C0E8CD240664D714E98; __jda=122270672.1525165158303683191212.1525165158.1539236656.1539262623.646; __jdb=122270672.1.1525165158303683191212|646.1539262623; shshshsID=4c92e3988b1a10b8ae0549f695d71690_1_1539262626958\"\n}', 'UTF8');
INSERT INTO `zd_interface` VALUES (3, 36, '接口绕弯儿无', 'http', '绕弯儿无r', 'POST', '111', '');
INSERT INTO `zd_interface` VALUES (4, 36, '接口22222222222222222', 'http', '222222222222222', 'POST', '22222222', '');
INSERT INTO `zd_interface` VALUES (5, 36, '阿里云地址接口，入参a', 'http', 'http://gc.ditu.aliyun.com/geocoding', 'POST', '{}', 'GBK');
INSERT INTO `zd_interface` VALUES (6, 36, '淘宝电话接口，入参tel', 'http', 'https://tcc.taobao.com/cc/json/mobile_tel_segment.htm', 'POST', '{\n\n\n}', 'GBK');
INSERT INTO `zd_interface` VALUES (7, 36, '接口w\'er\'we', 'http', '任务热无二无', 'POST', '儿我', 'GBK');
INSERT INTO `zd_interface` VALUES (10, 37, '本地接口', 'http', 'http://127.0.0.1:6060', 'GET', '{\n \"cookie\":\"zdPIN=34F7B6491123A0DBE7D34FD1B22C23CA754FEFCD3DE696A7448FFD928B7B6131CEE9A3C8814D9E0302262F9A16945A819FAE7042DF740AA01EAF91AE8F9AB33481673CC5DE3C6B84579F2EACF300CBF143C2B23CEDC9E027F750B4AAE3FECEBE\"\n\n}', 'UTF8');
INSERT INTO `zd_interface` VALUES (11, 35, '百度接口', 'http', 'https://www.baidu.com', 'POST', '{}', 'UTF8');
INSERT INTO `zd_interface` VALUES (13, 35, '网易接口', 'http', 'https://www.163.com', 'POST', '', 'GBK');
INSERT INTO `zd_interface` VALUES (14, 1, '23', 'http', '333', 'POST', '33', '');
INSERT INTO `zd_interface` VALUES (15, 49, 'http://127.0.0.1:6060/data/test/domain.json', 'http', 'http://127.0.0.1:6060/data/test/domain.json', 'GET', '{\n \"cookie\":\"zdPIN= 34F7B6491123A0DBE7D34FD1B22C23CA754FEFCD3DE696A7448FFD928B7B6131CEE9A3C8814D9E0302262F9A16945A819FAE7042DF740AA01EAF91AE8F9AB33481673CC5DE3C6B84579F2EACF300CBF143C2B23CEDC9E027F750B4AAE3FECEBE\"\n\n}', 'UTF8');
INSERT INTO `zd_interface` VALUES (16, 49, 'http://127.0.0.1', 'http', 'http://127.0.0.1', 'POST', '', 'UTF8');
INSERT INTO `zd_interface` VALUES (18, 50, '新接口', 'http', 'http://', 'GET', '', 'UTF8');
INSERT INTO `zd_interface` VALUES (19, 50, '新接口2', 'http', 'http://www.163.com', 'POST', '', 'UTF8');
COMMIT;

-- ----------------------------
-- Table structure for zd_plugin
-- ----------------------------
DROP TABLE IF EXISTS `zd_plugin`;
CREATE TABLE `zd_plugin` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `home` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zd_plugin
-- ----------------------------
BEGIN;
INSERT INTO `zd_plugin` VALUES (1, 'jmeter', '/developer/servers/apache-jmeter-5.0');
INSERT INTO `zd_plugin` VALUES (2, 'python', '/Library/Frameworks/Python.framework/Versions/3.7');
INSERT INTO `zd_plugin` VALUES (3, 'jekins', '');
COMMIT;

-- ----------------------------
-- Table structure for zd_project
-- ----------------------------
DROP TABLE IF EXISTS `zd_project`;
CREATE TABLE `zd_project` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `creator` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zd_project
-- ----------------------------
BEGIN;
INSERT INTO `zd_project` VALUES (1, 'x的项目：测\\试、``‘’\'\\\'项目1', 'xujia');
INSERT INTO `zd_project` VALUES (34, 'x的项目：用来测试功能', 'xujia');
INSERT INTO `zd_project` VALUES (35, 'x的项目：用来测试功能2', 'xujia');
INSERT INTO `zd_project` VALUES (36, 'x的项目：大项目-真实可用的数据', 'xujia');
INSERT INTO `zd_project` VALUES (37, 'x的项目：测试项目', 'xujia');
INSERT INTO `zd_project` VALUES (38, 'w的项目：together with u', 'wanghai');
INSERT INTO `zd_project` VALUES (42, 'l的项目：哎哟~不错哟……%￥#@', 'limaogui');
INSERT INTO `zd_project` VALUES (49, 'w的测试项目', 'wanghai');
INSERT INTO `zd_project` VALUES (50, '1', 'wanghai');
INSERT INTO `zd_project` VALUES (51, '2', 'wanghai');
INSERT INTO `zd_project` VALUES (52, '3', 'wanghai');
INSERT INTO `zd_project` VALUES (53, '4', 'wanghai');
COMMIT;

-- ----------------------------
-- Table structure for zd_project_user
-- ----------------------------
DROP TABLE IF EXISTS `zd_project_user`;
CREATE TABLE `zd_project_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(256) NOT NULL,
  `project_id` bigint(11) NOT NULL,
  `role` smallint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zd_project_user
-- ----------------------------
BEGIN;
INSERT INTO `zd_project_user` VALUES (1, 'wanghai', 37, 1);
INSERT INTO `zd_project_user` VALUES (3, 'xujia', 1, 0);
INSERT INTO `zd_project_user` VALUES (4, 'xujia', 34, 0);
INSERT INTO `zd_project_user` VALUES (5, 'xujia', 35, 0);
INSERT INTO `zd_project_user` VALUES (6, 'xujia', 36, 0);
INSERT INTO `zd_project_user` VALUES (7, 'xujia', 37, 0);
INSERT INTO `zd_project_user` VALUES (8, 'wanghai', 38, 0);
INSERT INTO `zd_project_user` VALUES (9, 'limaogui', 42, 0);
INSERT INTO `zd_project_user` VALUES (12, 'limaogui', 38, 1);
INSERT INTO `zd_project_user` VALUES (19, 'wanghai', 49, 0);
INSERT INTO `zd_project_user` VALUES (20, 'wanghai', 50, 0);
INSERT INTO `zd_project_user` VALUES (21, 'wanghai', 51, 0);
INSERT INTO `zd_project_user` VALUES (22, 'wanghai', 52, 0);
INSERT INTO `zd_project_user` VALUES (23, 'wanghai', 53, 0);
COMMIT;

-- ----------------------------
-- Table structure for zd_task
-- ----------------------------
DROP TABLE IF EXISTS `zd_task`;
CREATE TABLE `zd_task` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `con` varchar(256) NOT NULL,
  `cases` text,
  `lastrun` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id_index` (`project_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zd_task
-- ----------------------------
BEGIN;
INSERT INTO `zd_task` VALUES (1, 36, '1111', '', '', NULL);
INSERT INTO `zd_task` VALUES (3, 49, '新任务12', '', NULL, NULL);
INSERT INTO `zd_task` VALUES (4, 49, '新任务', '0 0 2 * * ?', '27,25,24,23,22,21,20,19,18', NULL);
INSERT INTO `zd_task` VALUES (5, 49, '新任务(每天凌晨两点)', '0 0 2 ? * SAT', '26,27', NULL);
INSERT INTO `zd_task` VALUES (6, 49, '可用的新任务', '0 0/2 * * * ?', '23,26', 'af4a98c0e27611e8946f8c8590a564f1');
INSERT INTO `zd_task` VALUES (7, 49, '可用的新任务2', '0 0/1 * * * ?', '26,18', 'e0e4a7e0e27611e8946f8c8590a564f1');
COMMIT;

-- ----------------------------
-- Table structure for zd_task_result
-- ----------------------------
DROP TABLE IF EXISTS `zd_task_result`;
CREATE TABLE `zd_task_result` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `tid` bigint(11) NOT NULL,
  `rid` varchar(64) NOT NULL,
  `result` mediumtext NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `start` timestamp NULL DEFAULT NULL,
  `end` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for zd_user
-- ----------------------------
DROP TABLE IF EXISTS `zd_user`;
CREATE TABLE `zd_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `group` varchar(128) NOT NULL,
  `pwd` varchar(2048) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zd_user
-- ----------------------------
BEGIN;
INSERT INTO `zd_user` VALUES (1, 'xujia', 'admin', 'a774d382529bfcde5596077bcb2d344c');
INSERT INTO `zd_user` VALUES (2, 'zdtest', 'admin', 'a774d382529bfcde5596077bcb2d344c');
INSERT INTO `zd_user` VALUES (5, 'wanghai', 'zduser', '19ac52e820d5f0d29f6b48cb25ffe47c');
INSERT INTO `zd_user` VALUES (6, 'limaogui', 'zduser', 'e10adc3949ba59abbe56e057f20f883e');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
