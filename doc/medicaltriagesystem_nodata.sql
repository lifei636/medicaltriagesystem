/*
Navicat MySQL Data Transfer

Source Server         : 235
Source Server Version : 50130
Source Host           : 192.168.0.235:3306
Source Database       : medicaltriagesystem

Target Server Type    : MYSQL
Target Server Version : 50130
File Encoding         : 65001

Date: 2018-09-11 14:48:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data_connection
-- ----------------------------
DROP TABLE IF EXISTS `data_connection`;
CREATE TABLE `data_connection` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dbType` varchar(50) NOT NULL DEFAULT 'mysql' COMMENT '数据库类型',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `dbName` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of data_connection
-- ----------------------------
INSERT INTO `data_connection` VALUES ('1', 'Oracle', 'mhcall', 'mhcall', 'HIS', 'orcl', '192.168.0.99');

-- ----------------------------
-- Table structure for db_sql
-- ----------------------------
DROP TABLE IF EXISTS `db_sql`;
CREATE TABLE `db_sql` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `db_source_id` int(11) NOT NULL,
  `outsql` text NOT NULL,
  `interval` int(11) NOT NULL,
  `last_time` datetime NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_FK_db_sql` (`db_source_id`) USING BTREE,
  CONSTRAINT `db_sql_ibfk_1` FOREIGN KEY (`db_source_id`) REFERENCES `data_connection` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of db_sql
-- ----------------------------
INSERT INTO `db_sql` VALUES ('1', '1', 'select * from doctor', '10', '2017-08-31 17:36:46', '1');
INSERT INTO `db_sql` VALUES ('2', '1', 'select patient_id,queue_type_source_id,patient_name,source_code,register_id,queue_num,sub_queue_order,sub_queue_type,0 as time_interval,doctor_source_id,is_deleted,   to_char(fre_date,\'yyyy-mm-dd hh24:mi:ss\') as fre_date from patient_queue', '1', '2017-09-08 09:45:11', '4');
INSERT INTO `db_sql` VALUES ('3', '1', 'select * from queue_type', '10', '2017-09-08 09:55:05', '2');
INSERT INTO `db_sql` VALUES ('4', '1', 'select * from rlt_doctor2queue_type', '10', '2017-10-25 10:19:16', '3');

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor` (
  `doctor_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` varchar(100) NOT NULL,
  `db_source_id` int(11) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `department` varchar(50) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `description` varchar(10000) DEFAULT NULL,
  `p_doctor_id` int(11) DEFAULT NULL,
  `call_rule` text,
  `opr_time` varchar(50) NOT NULL,
  PRIMARY KEY (`doctor_id`),
  UNIQUE KEY `login_id` (`login_id`) USING BTREE,
  KEY `IX_FK_doctordoctor` (`p_doctor_id`) USING BTREE,
  CONSTRAINT `doctor_ibfk_1` FOREIGN KEY (`p_doctor_id`) REFERENCES `doctor` (`doctor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of doctor
-- ----------------------------


-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `db_source_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `type` int(11) NOT NULL,
  `sqlstring` text NOT NULL,
  `Search_fields` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `interval` int(11) NOT NULL,
  `last_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_FK_db_sql` (`db_source_id`) USING BTREE,
  CONSTRAINT `item_ibfk_1` FOREIGN KEY (`db_source_id`) REFERENCES `souce` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of item
-- ----------------------------

-- ----------------------------
-- Table structure for pager
-- ----------------------------
DROP TABLE IF EXISTS `pager`;
CREATE TABLE `pager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `doctor_id` int(11) DEFAULT NULL,
  `login_time` datetime NOT NULL,
  `triage_id` int(11) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `display_name` varchar(50) NOT NULL,
  `ip` varchar(15) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `state` tinyint(4) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `call_rule` text,
  `call_pass_first_flag` int(11) NOT NULL DEFAULT '0',
  `call_pass_rule_flag` int(11) NOT NULL DEFAULT '0',
  `call_return_first_flag` int(11) NOT NULL DEFAULT '0',
  `call_return_rule_flag` int(11) NOT NULL DEFAULT '0',
  `show_visiting` varchar(255) DEFAULT NULL,
  `show_visiting_nmb` varchar(255) DEFAULT NULL,
  `show_call1` int(4) DEFAULT NULL,
  `show_call2` int(4) DEFAULT NULL,
  `show_call3` int(4) DEFAULT NULL,
  `show_call4` int(4) DEFAULT NULL,
  `show_call_time` datetime DEFAULT NULL,
  `show_login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_FK_pager` (`doctor_id`) USING BTREE,
  KEY `IX_FK_pager2triage` (`triage_id`) USING BTREE,
  CONSTRAINT `pager_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `pager_ibfk_2` FOREIGN KEY (`triage_id`) REFERENCES `triage` (`triage_id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pager
-- ----------------------------


-- ----------------------------
-- Table structure for patient_code
-- ----------------------------
DROP TABLE IF EXISTS `patient_code`;
CREATE TABLE `patient_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` longtext NOT NULL,
  `patient_queue_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_FK_patient_queuepatient_code` (`patient_queue_id`) USING BTREE,
  CONSTRAINT `patient_code_ibfk_1` FOREIGN KEY (`patient_queue_id`) REFERENCES `patient_queue` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of patient_code
-- ----------------------------


-- ----------------------------
-- Table structure for patient_queue
-- ----------------------------
DROP TABLE IF EXISTS `patient_queue`;
CREATE TABLE `patient_queue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `db_source_id` int(11) DEFAULT NULL,
  `patient_name` varchar(200) DEFAULT NULL,
  `queue_type_id` int(11) NOT NULL,
  `register_id` varchar(100) NOT NULL,
  `queue_num` varchar(100) NOT NULL,
  `sub_queue_order` int(4) NOT NULL,
  `sub_queue_type` varchar(100) DEFAULT NULL,
  `pager_id` int(11) DEFAULT NULL,
  `doctor_id` int(11) DEFAULT NULL,
  `call_time` datetime DEFAULT NULL,
  `is_priority` int(4) DEFAULT NULL,
  `return_flag` int(11) DEFAULT NULL,
  `is_display` int(4) DEFAULT '2',
  `time_interval` int(4) DEFAULT NULL,
  `fre_date` datetime DEFAULT NULL,
  `opr_time` varchar(50) DEFAULT NULL,
  `content` text,
  `state_custom` varchar(50) DEFAULT NULL,
  `patient_id` varchar(100) NOT NULL,
  `queue_type_source_id` varchar(100) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `state_patient` int(4) NOT NULL DEFAULT '0',
  `call_count` int(11) DEFAULT NULL,
  `source_id` int(11) DEFAULT NULL,
  `delay_time` datetime DEFAULT NULL,
  `operator_type` int(4) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT '0',
  `caller` varchar(20) DEFAULT NULL,
  `istype` varchar(20) DEFAULT NULL,
  `patient_source_code` varchar(255) DEFAULT NULL,
  `nextshow` varchar(255) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `patient_id` (`patient_id`) USING BTREE,
  UNIQUE KEY `parent_id` (`parent_id`) USING BTREE,
  KEY `IX_FK_patient_queue` (`queue_type_id`) USING BTREE,
  KEY `IX_FK_patient_queue2` (`doctor_id`) USING BTREE,
  KEY `IX_FK_patient_queue3` (`pager_id`) USING BTREE,
  CONSTRAINT `patient_queue_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `patient_queue` (`id`),
  CONSTRAINT `patient_queue_ibfk_2` FOREIGN KEY (`pager_id`) REFERENCES `pager` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `patient_queue_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `patient_queue_ibfk_4` FOREIGN KEY (`queue_type_id`) REFERENCES `queue_type` (`queue_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of patient_queue
-- ----------------------------

-- ----------------------------
-- Table structure for queue_type
-- ----------------------------
DROP TABLE IF EXISTS `queue_type`;
CREATE TABLE `queue_type` (
  `queue_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `triage_id` int(11) DEFAULT NULL,
  `db_source_id` int(11) DEFAULT NULL,
  `source_id` varchar(100) DEFAULT NULL,
  `reserve_numlist` text,
  `is_reorder` int(4) DEFAULT '1',
  `is_ckin_order` int(4) DEFAULT '2',
  `is_checkin` int(4) DEFAULT '2',
  `is_pretriage` int(4) DEFAULT '2',
  `avg_minute` int(11) DEFAULT '0',
  `name` varchar(100) NOT NULL,
  `displayname` varchar(255) NOT NULL,
  `is_reserve` int(4) DEFAULT '2',
  `pass_first_flag` int(11) NOT NULL DEFAULT '0',
  `pass_flag_step` int(11) NOT NULL DEFAULT '0',
  `queue_num_max` double DEFAULT NULL,
  `queue_num_call_max` double DEFAULT NULL,
  `pass_max` int(11) DEFAULT NULL,
  `opr_time` varchar(50) NOT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `agin_flag_step` int(11) NOT NULL DEFAULT '0',
  `agin_first_flag` int(11) NOT NULL DEFAULT '0',
  `show_wait` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`queue_type_id`),
  UNIQUE KEY `source_id` (`source_id`) USING BTREE,
  KEY `IX_FK_queue_type` (`triage_id`) USING BTREE,
  CONSTRAINT `queue_type_ibfk_1` FOREIGN KEY (`triage_id`) REFERENCES `triage` (`triage_id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of queue_type
-- ----------------------------

-- ----------------------------
-- Table structure for rlt_doctor2queue_type
-- ----------------------------
DROP TABLE IF EXISTS `rlt_doctor2queue_type`;
CREATE TABLE `rlt_doctor2queue_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `doctor_id` int(11) NOT NULL,
  `queue_type_id` int(11) NOT NULL,
  `onduty` varchar(50) NOT NULL,
  `is_custom` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_FK_rlt_doctor2queue_type1` (`queue_type_id`) USING BTREE,
  KEY `IX_FK_rlt_doctor2queue_type` (`doctor_id`) USING BTREE,
  CONSTRAINT `rlt_doctor2queue_type_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rlt_doctor2queue_type_ibfk_2` FOREIGN KEY (`queue_type_id`) REFERENCES `queue_type` (`queue_type_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rlt_doctor2queue_type
-- ----------------------------


-- ----------------------------
-- Table structure for rlt_pager2queue_type
-- ----------------------------
DROP TABLE IF EXISTS `rlt_pager2queue_type`;
CREATE TABLE `rlt_pager2queue_type` (
  `pager_id` int(11) NOT NULL,
  `queue_type_id` int(11) NOT NULL,
  `onduty` varchar(7) NOT NULL,
  `is_custom` tinyint(4) NOT NULL,
  PRIMARY KEY (`pager_id`,`queue_type_id`),
  KEY `IX_FK_rlt_pager2queue_type1` (`queue_type_id`) USING BTREE,
  CONSTRAINT `rlt_pager2queue_type_ibfk_1` FOREIGN KEY (`pager_id`) REFERENCES `pager` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rlt_pager2queue_type_ibfk_2` FOREIGN KEY (`queue_type_id`) REFERENCES `queue_type` (`queue_type_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rlt_pager2queue_type
-- ----------------------------


-- ----------------------------
-- Table structure for rlt_pager2terminal
-- ----------------------------
DROP TABLE IF EXISTS `rlt_pager2terminal`;
CREATE TABLE `rlt_pager2terminal` (
  `pager_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`pager_id`,`id`),
  KEY `IX_FK_rlt_pager2terminal_terminal` (`id`) USING BTREE,
  CONSTRAINT `rlt_pager2terminal_ibfk_1` FOREIGN KEY (`id`) REFERENCES `terminal` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rlt_pager2terminal_ibfk_2` FOREIGN KEY (`pager_id`) REFERENCES `pager` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rlt_pager2terminal
-- ----------------------------


-- ----------------------------
-- Table structure for souce
-- ----------------------------
DROP TABLE IF EXISTS `souce`;
CREATE TABLE `souce` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dbType` int(2) NOT NULL DEFAULT '0' COMMENT '数据库类型',
  `driver` varchar(255) NOT NULL COMMENT '数据库驱动',
  `url` varchar(255) NOT NULL COMMENT '数据库连接',
  `username` varchar(255) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of souce
-- ----------------------------

-- ----------------------------
-- Table structure for sync_data_log
-- ----------------------------
DROP TABLE IF EXISTS `sync_data_log`;
CREATE TABLE `sync_data_log` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `log_name` varchar(100) NOT NULL,
  `log_msg` varchar(1000) NOT NULL,
  `log_exception` text NOT NULL,
  `log_time` datetime NOT NULL,
  `log_type` int(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sync_data_log
-- ----------------------------

-- ----------------------------
-- Table structure for terminal
-- ----------------------------
DROP TABLE IF EXISTS `terminal`;
CREATE TABLE `terminal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `triage_id` int(11) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `display_name` varchar(255) NOT NULL,
  `ip` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_FK_terminal` (`triage_id`) USING BTREE,
  CONSTRAINT `terminal_ibfk_1` FOREIGN KEY (`triage_id`) REFERENCES `triage` (`triage_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of terminal
-- ----------------------------


-- ----------------------------
-- Table structure for tfw_attach
-- ----------------------------
DROP TABLE IF EXISTS `tfw_attach`;
CREATE TABLE `tfw_attach` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `URL` text,
  `STATUS` int(11) DEFAULT NULL,
  `CREATER` int(11) DEFAULT NULL,
  `CREATETIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_attach
-- ----------------------------

-- ----------------------------
-- Table structure for tfw_dept
-- ----------------------------
DROP TABLE IF EXISTS `tfw_dept`;
CREATE TABLE `tfw_dept` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NUM` int(11) DEFAULT NULL,
  `PID` int(11) DEFAULT NULL,
  `SIMPLENAME` varchar(45) DEFAULT NULL,
  `FULLNAME` varchar(255) DEFAULT NULL,
  `TIPS` varchar(255) DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_dept
-- ----------------------------
INSERT INTO `tfw_dept` VALUES ('1', '1', '0', '信息科', '信息科', '', '1');

-- ----------------------------
-- Table structure for tfw_dict
-- ----------------------------
DROP TABLE IF EXISTS `tfw_dict`;
CREATE TABLE `tfw_dict` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(255) DEFAULT NULL,
  `NUM` int(11) DEFAULT NULL,
  `PID` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `TIPS` varchar(255) DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_dict
-- ----------------------------
INSERT INTO `tfw_dict` VALUES ('1', '101', '0', '0', '性别', null, '0');
INSERT INTO `tfw_dict` VALUES ('2', '101', '1', '1', '男', null, '1');
INSERT INTO `tfw_dict` VALUES ('3', '101', '2', '1', '女', null, '0');
INSERT INTO `tfw_dict` VALUES ('5', '901', '0', '0', '账号状态', null, '0');
INSERT INTO `tfw_dict` VALUES ('6', '901', '1', '5', '启用', null, '0');
INSERT INTO `tfw_dict` VALUES ('7', '901', '2', '5', '冻结', null, '0');
INSERT INTO `tfw_dict` VALUES ('8', '901', '3', '5', '待审核', null, '0');
INSERT INTO `tfw_dict` VALUES ('9', '901', '4', '5', '审核拒绝', null, '0');
INSERT INTO `tfw_dict` VALUES ('10', '901', '5', '5', '已删除', null, '0');
INSERT INTO `tfw_dict` VALUES ('11', '902', '0', '0', '状态', null, '0');
INSERT INTO `tfw_dict` VALUES ('12', '902', '1', '11', '启用', null, '0');
INSERT INTO `tfw_dict` VALUES ('13', '902', '2', '11', '禁用', null, '0');
INSERT INTO `tfw_dict` VALUES ('14', '102', '0', '0', '公告类型', null, '0');
INSERT INTO `tfw_dict` VALUES ('15', '102', '10', '14', '通知公告', null, '0');
INSERT INTO `tfw_dict` VALUES ('16', '102', '9', '14', '发布计划', null, '0');
INSERT INTO `tfw_dict` VALUES ('17', '903', '0', '0', '审核状态', null, '0');
INSERT INTO `tfw_dict` VALUES ('18', '903', '1', '17', '待审核', null, '0');
INSERT INTO `tfw_dict` VALUES ('19', '903', '2', '17', '审核拒绝', null, '0');
INSERT INTO `tfw_dict` VALUES ('20', '903', '3', '17', '审核通过', null, '0');
INSERT INTO `tfw_dict` VALUES ('44', '102', '1', '14', '发布测试', null, '0');
INSERT INTO `tfw_dict` VALUES ('45', '103', '0', '0', '分诊类型', '', '0');
INSERT INTO `tfw_dict` VALUES ('46', '103', '1', '45', '按医生', '', '0');
INSERT INTO `tfw_dict` VALUES ('47', '103', '2', '45', '按叫号器', '', '0');
INSERT INTO `tfw_dict` VALUES ('48', '104', '0', '0', '是否报道', '', '1');
INSERT INTO `tfw_dict` VALUES ('49', '104', '1', '48', '报道', '', '0');
INSERT INTO `tfw_dict` VALUES ('50', '104', '2', '48', '不报道', '', '0');
INSERT INTO `tfw_dict` VALUES ('51', '105', '0', '0', '是否预分诊', '', '0');
INSERT INTO `tfw_dict` VALUES ('52', '105', '1', '51', '预分诊', '', '0');
INSERT INTO `tfw_dict` VALUES ('53', '105', '2', '51', '不预分诊', '', '0');
INSERT INTO `tfw_dict` VALUES ('54', '106', '0', '0', '是否重新排号', '', '0');
INSERT INTO `tfw_dict` VALUES ('55', '106', '1', '54', '重新排号', '', '0');
INSERT INTO `tfw_dict` VALUES ('56', '106', '2', '54', '不重新排号', '', '0');
INSERT INTO `tfw_dict` VALUES ('57', '107', '0', '0', '是否预约', '', '0');
INSERT INTO `tfw_dict` VALUES ('58', '107', '1', '57', '预约', '', '0');
INSERT INTO `tfw_dict` VALUES ('59', '107', '2', '57', '不预约', '', '0');
INSERT INTO `tfw_dict` VALUES ('63', '109', '0', '0', '语句类型', '', '0');
INSERT INTO `tfw_dict` VALUES ('64', '109', '1', '63', '医生信息', '', '0');
INSERT INTO `tfw_dict` VALUES ('65', '109', '2', '63', '队列类型', '', '0');
INSERT INTO `tfw_dict` VALUES ('66', '109', '3', '63', '医生与队列关系', '', '0');
INSERT INTO `tfw_dict` VALUES ('67', '109', '4', '63', '患者挂号信息', '', '0');
INSERT INTO `tfw_dict` VALUES ('68', '110', '0', '0', '数据库类型', '', '0');
INSERT INTO `tfw_dict` VALUES ('69', '110', '1', '68', 'MYSQL', '', '0');
INSERT INTO `tfw_dict` VALUES ('70', '110', '2', '68', 'ORACLE', '', '0');
INSERT INTO `tfw_dict` VALUES ('71', '111', '0', '0', '是否报道重新排号', '', '0');
INSERT INTO `tfw_dict` VALUES ('72', '111', '1', '71', '报道重新排号', '', '0');
INSERT INTO `tfw_dict` VALUES ('73', '111', '2', '71', '报道不重新排号', '', '0');
INSERT INTO `tfw_dict` VALUES ('74', '112', '0', '0', '排号模式', '', '1');
INSERT INTO `tfw_dict` VALUES ('75', '112', '1', '74', '按队列排号', '', '2');
INSERT INTO `tfw_dict` VALUES ('76', '112', '2', '74', '按叫号器排号', '', '2');
INSERT INTO `tfw_dict` VALUES ('77', '113', '0', '0', '打印模式', null, '0');
INSERT INTO `tfw_dict` VALUES ('78', '113', '1', '77', '手动打印', null, '0');
INSERT INTO `tfw_dict` VALUES ('79', '113', '2', '77', '自动打印', null, '0');

-- ----------------------------
-- Table structure for tfw_generate
-- ----------------------------
DROP TABLE IF EXISTS `tfw_generate`;
CREATE TABLE `tfw_generate` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `REALPATH` varchar(255) DEFAULT NULL,
  `PACKAGENAME` varchar(255) DEFAULT NULL,
  `MODELNAME` varchar(255) DEFAULT NULL,
  `TABLENAME` varchar(255) DEFAULT NULL,
  `PKNAME` varchar(255) DEFAULT NULL,
  `TIPS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_generate
-- ----------------------------
INSERT INTO `tfw_generate` VALUES ('4', 'triage', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'Triage', 'triage', 'triage_id', null);
INSERT INTO `tfw_generate` VALUES ('5', 'doctor', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'Doctor', 'doctor', 'doctor_id', null);
INSERT INTO `tfw_generate` VALUES ('6', 'pager', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'Pager', 'pager', 'id', null);
INSERT INTO `tfw_generate` VALUES ('7', 'queuetype', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'QueueType', 'queue_type', 'queue_type_id', null);
INSERT INTO `tfw_generate` VALUES ('8', 'patientcode', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'PatientCode', 'patient_code', 'id', null);
INSERT INTO `tfw_generate` VALUES ('9', 'patientqueue', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'PatientQueue', 'patient_queue', 'id', null);
INSERT INTO `tfw_generate` VALUES ('10', 'doctor2queuetype', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'Doctor2queuetype', 'rlt_doctor2queue_type', 'id', null);
INSERT INTO `tfw_generate` VALUES ('11', 'pager2queuetype', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'Pager2queuetype', 'rlt_pager2queue_type', 'pager_id,queue_type_id', null);
INSERT INTO `tfw_generate` VALUES ('12', 'terminal', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'Terminal', 'terminal', 'id', null);
INSERT INTO `tfw_generate` VALUES ('13', 'pager2terminal', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'Pager2terminal', 'rlt_pager2terminal', 'pager_id,id', null);
INSERT INTO `tfw_generate` VALUES ('14', 'dataConnection', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'DataConnection', 'data_connection', 'id', null);
INSERT INTO `tfw_generate` VALUES ('15', 'dbSql', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'DbSql', 'db_sql', 'id', null);
INSERT INTO `tfw_generate` VALUES ('16', 'source', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'Source', 'souce', 'id', null);
INSERT INTO `tfw_generate` VALUES ('17', 'item', 'D:\\development\\workspace\\medicaltriagesystem', 'com.shine', 'Item', 'item', 'id', null);

-- ----------------------------
-- Table structure for tfw_login_log
-- ----------------------------
DROP TABLE IF EXISTS `tfw_login_log`;
CREATE TABLE `tfw_login_log` (
  `ID` int(65) NOT NULL AUTO_INCREMENT,
  `LOGNAME` varchar(255) DEFAULT NULL,
  `USERID` varchar(255) DEFAULT NULL,
  `CLASSNAME` varchar(255) DEFAULT NULL,
  `METHOD` text,
  `CREATETIME` datetime DEFAULT NULL,
  `SUCCEED` varchar(255) DEFAULT NULL,
  `MESSAGE` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for tfw_menu
-- ----------------------------
DROP TABLE IF EXISTS `tfw_menu`;
CREATE TABLE `tfw_menu` (
  `ID` int(65) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(255) DEFAULT NULL,
  `PCODE` varchar(255) DEFAULT NULL,
  `ALIAS` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `ICON` varchar(255) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `NUM` int(65) DEFAULT NULL,
  `LEVELS` int(65) DEFAULT NULL,
  `SOURCE` text,
  `PATH` varchar(255) DEFAULT NULL,
  `TIPS` varchar(255) DEFAULT NULL,
  `STATUS` int(65) DEFAULT NULL,
  `ISOPEN` varchar(255) DEFAULT NULL,
  `ISTEMPLATE` varchar(255) DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=178 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_menu
-- ----------------------------
INSERT INTO `tfw_menu` VALUES ('1', 'system', '0', null, '系统管理', 'fa-cog', null, '9', '1', null, null, null, '1', '1', '0', '3');
INSERT INTO `tfw_menu` VALUES ('2', 'role', 'system', null, '角色管理', 'fa-key', '/role/', '2', '2', null, null, null, '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('3', 'role_add', 'role', 'addex', '角色新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/role/add', '1', '3', null, 'role_add.html', '800*340', '1', '0', null, '2');
INSERT INTO `tfw_menu` VALUES ('4', 'role_edit', 'role', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/role/edit', '2', '3', null, 'role_edit.html', '800*340', '1', '0', '0', '1');
INSERT INTO `tfw_menu` VALUES ('5', 'role_remove', 'role', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/role/remove', '3', '3', null, null, null, '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('6', 'role_view', 'role', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/role/view', '4', '3', null, 'role_view.html', '800*340', '1', null, null, '1');
INSERT INTO `tfw_menu` VALUES ('7', 'role_authority', 'role', 'authority', '权限配置', 'btn btn-xs btn-white | fa fa-wrench  bigger-120', '/role/authority', '5', '3', null, 'role_authority.html', '350*500', '1', '0', null, '2');
INSERT INTO `tfw_menu` VALUES ('8', 'user', 'system', null, '用户管理', 'fa-user', '/user/', '1', '2', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('9', 'user_add', 'user', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/user/add', '1', '3', null, 'user_add.html', '800*430', '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('10', 'user_edit', 'user', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/user/edit', '2', '3', null, 'user_edit.html', '800*430', '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('11', 'user_del', 'user', 'remove', '删除', 'btn btn-xs btn-white | fa fa fa-times bigger-120', '/user/del', '3', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('12', 'user_view', 'user', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/user/view', '4', '3', null, 'user_view.html', '800*390', '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('13', 'user_audit', 'user', 'audit', '审核', 'btn btn-xs btn-white | fa fa-user  bigger-120', '{\"status\":\"3\"}', '5', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('14', 'user_audit_ok', 'user_audit', 'ok', '通过', 'btn btn-xs btn-white | fa fa-check  bigger-120', '/user/auditOk', '1', '4', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('15', 'user_audit_refuse', 'user_audit', 'refuse', '拒绝', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/user/auditRefuse', '2', '4', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('16', 'user_audit_back', 'user_audit', 'back', '返回', 'btn btn-xs btn-white | fa fa-undo  bigger-120', null, '3', '4', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('17', 'user_reset', 'user', 'reset', '重置密码', 'btn btn-xs btn-white | fa fa-key  bigger-120', '/user/reset', '6', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('18', 'user_ban', 'user', 'frozen', '冻结', 'btn btn-xs btn-white | fa fa-ban  bigger-120', '/user/ban', '7', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('19', 'user_recycle', 'user', 'recycle', '回收站', 'btn btn-xs btn-white | fa fa-recycle  bigger-120', '{\"status\":\"5\"}', '8', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('20', 'user_recycle_restore', 'user_recycle', 'restore', '还原', 'btn btn-xs btn-white | fa fa-refresh  bigger-120', '/user/restore', '1', '4', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('21', 'user_recycle_remove', 'user_recycle', 'remove', '彻底删除', 'btn btn-xs btn-white  btn-danger | fa fa fa-times bigger-120', '/user/remove', '2', '4', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('22', 'user_recycle_back', 'user_recycle', 'back', '返回', 'btn btn-xs btn-white | fa fa-undo  bigger-120', null, '3', '4', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('23', 'user_roleAssign', 'user', 'assign', '角色分配', 'btn btn-xs btn-white | fa fa-users bigger-120', '/user/roleAssign', '9', '3', null, 'user_roleAssign.html', '350*500', '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('24', 'user_extrole', 'user', 'agent', '权限代理', 'btn btn-xs btn-white | fa fa-wrench  bigger-120', '/user/extrole', '10', '3', null, 'user_extrole.html', null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('25', 'menu', 'system', null, '菜单管理', 'fa-tasks', '/menu/', '3', '2', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('26', 'menu_add', 'menu', 'addex', '菜单新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/menu/add', '1', '3', null, 'menu_add.html', '800*430', '1', '0', '0', '1');
INSERT INTO `tfw_menu` VALUES ('27', 'menu_edit', 'menu', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/menu/edit', '2', '3', null, 'menu_edit.html', '800*430', '1', '0', '0', '1');
INSERT INTO `tfw_menu` VALUES ('28', 'menu_del', 'menu', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/menu/del', '3', '3', null, null, null, '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('29', 'menu_view', 'menu', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/menu/view', '4', '3', null, 'menu_view.html', '800*430', '1', '0', '0', '1');
INSERT INTO `tfw_menu` VALUES ('30', 'menu_recycle', 'menu', 'recycle', '回收站', 'btn btn-xs btn-white | fa fa-recycle  bigger-120', '{\"status\":\"2\"}', '5', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('31', 'menu_recycle_restore', 'menu_recycle', 'restore', '还原', 'btn btn-xs btn-white | fa fa-refresh  bigger-120', '/menu/restore', '1', '4', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('32', 'menu_recycle_remove', 'menu_recycle', 'remove', '彻底删除', 'btn btn-xs btn-white  btn-danger | fa fa fa-times bigger-120', '/menu/remove', '2', '4', null, null, null, '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('33', 'menu_recycle_back', 'menu_recycle', 'back', '返回', 'btn btn-xs btn-white | fa fa-undo  bigger-120', null, '3', '4', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('34', 'dict', 'system', null, '字典管理', 'fa fa-book', '/dict/', '4', '2', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('35', 'dict_add', 'dict', 'addex', '字典新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/dict/add', '1', '3', null, 'dict_add.html', '800*390', '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('36', 'dict_edit', 'dict', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/dict/edit', '2', '3', null, 'dict_edit.html', '800*390', '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('37', 'dict_remove', 'dict', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/dict/remove', '3', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('38', 'dict_view', 'dict', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/dict/view', '4', '3', null, 'dict_view.html', '800*390', '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('39', 'dept', 'system', null, '部门管理', 'fa fa-users', '/dept/', '5', '2', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('40', 'dept_add', 'dept', 'addex', '部门新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/dept/add', '1', '3', null, 'dept_add.html', '800*340', '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('41', 'dept_edit', 'dept', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/dept/edit', '2', '3', null, 'dept_edit.html', '800*340', '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('42', 'dept_remove', 'dept', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/dept/remove', '3', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('43', 'dept_view', 'dept', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/dept/view', '4', '3', null, 'dept_view.html', '800*340', '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('44', 'attach', 'system', null, '附件管理', 'fa fa-paperclip', '/attach/', '6', '2', null, 'attach.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('45', 'attach_add', 'attach', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/attach/add', '1', '3', null, 'attach_add.html', '800*450', '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('46', 'attach_edit', 'attach', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/attach/edit', '2', '3', null, 'attach_edit.html', '800*290', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('47', 'attach_remove', 'attach', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/attach/remove', '3', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('48', 'attach_view', 'attach', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/attach/view', '4', '3', null, 'attach_view.html', '800*450', '1', '0', '0', '1');
INSERT INTO `tfw_menu` VALUES ('49', 'attach_download', 'attach', 'download', '下载', 'btn btn-xs btn-white | fa fa-paperclip bigger-120', '/attach/download', '5', '3', null, null, null, '1', null, null, '0');
INSERT INTO `tfw_menu` VALUES ('56', 'parameter', 'system', null, '参数化管理', 'fa-tags', '/parameter/', '9', '2', null, 'parameter.html', null, '1', '0', '1', '0');
INSERT INTO `tfw_menu` VALUES ('57', 'parameter_add', 'parameter', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/parameter/add', '1', '3', null, 'parameter_add.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('58', 'parameter_edit', 'parameter', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/parameter/edit', '2', '3', null, 'parameter_edit.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('59', 'parameter_del', 'parameter', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/parameter/del', '3', '3', null, null, null, '1', '0', '0', '1');
INSERT INTO `tfw_menu` VALUES ('60', 'parameter_view', 'parameter', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/parameter/view', '4', '3', null, 'parameter_view.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('61', 'parameter_recycle', 'parameter', 'recycle', '回收站', 'btn btn-xs btn-white | fa fa-recycle  bigger-120', '{\"status\":\"5\"}', '5', '3', null, 'parameter_recycle.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('62', 'parameter_recycle_restore', 'parameter_recycle', 'restore', '还原', 'btn btn-xs btn-white | fa fa-refresh  bigger-120', '/parameter/restore', '1', '4', null, null, null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('63', 'parameter_recycle_remove', 'parameter_recycle', 'remove', '彻底删除', 'btn btn-xs btn-white  btn-danger | fa fa fa-times bigger-120', '/parameter/remove', '2', '4', null, null, null, '1', '0', '0', '1');
INSERT INTO `tfw_menu` VALUES ('64', 'parameter_recycle_back', 'parameter_recycle', 'back', '返回', 'btn btn-xs btn-white | fa fa-undo  bigger-120', null, '3', '4', null, null, null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('65', 'druid', 'system', null, '连接池监视', 'fa-arrows-v', '/druid', '10', '2', null, null, null, '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('81', 'log', 'system', null, '日志管理', 'fa-tasks', null, '11', '2', null, null, null, '1', '0', '0', '1');
INSERT INTO `tfw_menu` VALUES ('82', 'olog', 'log', null, '操作日志', 'fa-database', '/olog/', '1', '3', null, 'olog.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('83', 'llog', 'log', null, '登录日志', 'fa-sign-in', '/llog/', '2', '3', null, 'llog.html', null, '1', '0', '1', '0');
INSERT INTO `tfw_menu` VALUES ('84', 'olog_add', 'olog', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/olog/add', '1', '4', null, 'olog_add.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('85', 'olog_edit', 'olog', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/olog/edit', '2', '4', null, 'olog_edit.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('86', 'olog_remove', 'olog', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/olog/remove', '3', '4', null, null, null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('87', 'olog_view', 'olog', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/olog/view', '4', '4', null, 'olog_view.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('88', 'llog_add', 'llog', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/llog/add', '1', '4', null, 'llog_add.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('89', 'llog_edit', 'llog', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/llog/edit', '2', '4', null, 'llog_edit.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('90', 'llog_remove', 'llog', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/llog/remove', '3', '4', null, null, null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('91', 'llog_view', 'llog', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/llog/view', '4', '4', null, 'llog_view.html', null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('92', 'office', '0', null, '工作台', 'fa fa-desktop', null, '1', '1', null, null, null, '1', '0', '0', '0');
INSERT INTO `tfw_menu` VALUES ('98', 'online', 'system', '', '在线开发', 'fa-rocket', null, '12', '2', null, null, '800*520', '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('99', 'generate', 'online', null, '代码生成', 'fa-gavel', '/generate/', '1', '3', null, null, '800*520', '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('100', 'generate_add', 'generate', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/generate/add', '1', '4', null, null, '800*420', '1', '0', null, '3');
INSERT INTO `tfw_menu` VALUES ('101', 'generate_edit', 'generate', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/generate/edit', '2', '4', null, null, '800*420', '1', '0', null, '3');
INSERT INTO `tfw_menu` VALUES ('102', 'generate_remove', 'generate', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/generate/remove', '3', '4', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('103', 'generate_view', 'generate', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/generate/view', '4', '4', null, null, '800*420', '1', '0', null, '3');
INSERT INTO `tfw_menu` VALUES ('104', 'generate_gencode', 'generate', 'gencode', '代码生成', 'btn btn-xs btn-white | fa fa-gavel bigger-120', '/generate/gencode', '5', '4', null, null, '800*520', '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('105', 'triage', 'office', '', '分诊台管理', 'fa fa-bell', '/triage/', '2', '2', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('106', 'triage_add', 'triage', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/triage/add', '1', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('107', 'triage_remove', 'triage', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/triage/remove', '2', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('108', 'triage_edit', 'triage', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/triage/edit', '3', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('115', 'doctor', 'office', '', '医生管理', 'fa fa-bell', '/doctor/', '4', '2', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('116', 'doctor_add', 'doctor', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/doctor/add', '1', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('117', 'doctor_remove', 'doctor', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/doctor/remove', '2', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('118', 'doctor_edit', 'doctor', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/doctor/edit', '3', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('120', 'pager', 'office', '', '叫号器管理', 'fa fa-bell', '/pager/', '5', '2', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('121', 'pager_add', 'pager', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/pager/add', '1', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('122', 'pager_remove', 'pager', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/pager/remove', '2', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('123', 'pager_edit', 'pager', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/pager/edit', '3', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('125', 'queueType', 'office', '', '队列管理', 'fa fa-bell', '/queueType/', '6', '2', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('126', 'queueType_add', 'queueType', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/queueType/add', '1', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('127', 'queueType_remove', 'queueType', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/queueType/remove', '2', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('128', 'queueType_edit', 'queueType', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/queueType/edit', '3', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('130', 'triage_queuetype', 'triage', 'edit', '绑定队列', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/triage/triageQueuetype', '4', '3', null, null, '800*520', '1', '0', null, '2');
INSERT INTO `tfw_menu` VALUES ('132', 'pager_pagerqueuetype', 'pager', 'edit', '队列编辑', 'btn btn-xs btn-white | fa fa-pencil bigger-120', '/pager/pagerqueuetype', '4', '3', null, null, '800*520', '1', '1', null, '0');
INSERT INTO `tfw_menu` VALUES ('133', 'terminal', 'office', '', '屏幕管理', 'fa fa-bell', '/terminal/', '7', '2', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('134', 'terminal_add', 'terminal', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/terminal/add', '1', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('135', 'terminal_remove', 'terminal', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/terminal/remove', '2', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('136', 'terminal_edit', 'terminal', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/terminal/edit', '3', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('138', 'dataConnection', 'office', '', '数据源管理', 'fa fa-bell', '/dataConnection/', '8', '2', null, null, '800*520', '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('139', 'dataConnection_add', 'dataConnection', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/dataConnection/add', '1', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('140', 'dataConnection_remove', 'dataConnection', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/dataConnection/remove', '2', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('142', 'dbSql', 'office', '', '数据项管理', 'fa fa-bell', '/dbSql/', '9', '2', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('143', 'dbSql_add', 'dbSql', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/dbSql/add', '1', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('144', 'dbSql_remove', 'dbSql', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/dbSql/remove', '2', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('145', 'dbSql_edit', 'dbSql', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/dbSql/edit', '3', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('154', 'dataConnection_edit', 'dataConnection', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/dataConnection/edit', '3', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('169', 'syncDataLog', 'office', 'sync', '数据同步日志管理', 'fa fa-bell', '/syncDataLog/', '12', '2', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('170', 'syncDataLog_view', 'syncDataLog', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/syncDataLog/view', '1', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('171', 'syncDataLog_deletelog', 'syncDataLog', 'removeAll', '清空', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/syncDataLog/deletelog', '2', '3', null, null, '800*520', '1', '0', null, '11');
INSERT INTO `tfw_menu` VALUES ('172', 'syncDataLog_remove', 'syncDataLog', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/syncDataLog/remove', '3', '3', null, null, '800*520', '1', '0', null, '0');
INSERT INTO `tfw_menu` VALUES ('173', 'sync', 'terminal', 'syncTerminal', '同步屏幕', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/terminal/sync', '4', '3', null, null, '800*520', '1', '1', null, '2');
INSERT INTO `tfw_menu` VALUES ('176', 'terminal_edittriage', 'terminal', 'edittriage', '批量修改分诊台', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/terminal/edittriage', '5', '3', null, null, '400*260', '1', '0', null, '1');
INSERT INTO `tfw_menu` VALUES ('177', 'pager_pagerterminal', 'pager', 'edit', '屏幕编辑', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/pager/pagerterminal', '5', '3', null, null, '800*520', '1', '0', null, '0');

-- ----------------------------
-- Table structure for tfw_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `tfw_operation_log`;
CREATE TABLE `tfw_operation_log` (
  `ID` int(65) NOT NULL AUTO_INCREMENT,
  `LOGNAME` varchar(255) DEFAULT NULL,
  `USERID` varchar(255) DEFAULT NULL,
  `CLASSNAME` varchar(255) DEFAULT NULL,
  `METHOD` text,
  `CREATETIME` datetime DEFAULT NULL,
  `SUCCEED` varchar(255) DEFAULT NULL,
  `MESSAGE` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for tfw_parameter
-- ----------------------------
DROP TABLE IF EXISTS `tfw_parameter`;
CREATE TABLE `tfw_parameter` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(255) DEFAULT NULL,
  `NUM` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PARA` text,
  `TIPS` varchar(255) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_parameter
-- ----------------------------
INSERT INTO `tfw_parameter` VALUES ('1', '101', '100', '是否开启记录日志', '2', '1:是  2:否', '1', '10');

-- ----------------------------
-- Table structure for tfw_relation
-- ----------------------------
DROP TABLE IF EXISTS `tfw_relation`;
CREATE TABLE `tfw_relation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MENUID` int(11) DEFAULT NULL,
  `ROLEID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4588 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_relation
-- ----------------------------
INSERT INTO `tfw_relation` VALUES ('1244', '1', '3');
INSERT INTO `tfw_relation` VALUES ('1245', '62', '3');
INSERT INTO `tfw_relation` VALUES ('1246', '64', '3');
INSERT INTO `tfw_relation` VALUES ('1247', '72', '3');
INSERT INTO `tfw_relation` VALUES ('1248', '73', '3');
INSERT INTO `tfw_relation` VALUES ('1249', '74', '3');
INSERT INTO `tfw_relation` VALUES ('1250', '75', '3');
INSERT INTO `tfw_relation` VALUES ('1251', '76', '3');
INSERT INTO `tfw_relation` VALUES ('1252', '77', '3');
INSERT INTO `tfw_relation` VALUES ('1253', '78', '3');
INSERT INTO `tfw_relation` VALUES ('1254', '79', '3');
INSERT INTO `tfw_relation` VALUES ('1255', '80', '3');
INSERT INTO `tfw_relation` VALUES ('1384', '72', '6');
INSERT INTO `tfw_relation` VALUES ('1385', '73', '6');
INSERT INTO `tfw_relation` VALUES ('1386', '74', '6');
INSERT INTO `tfw_relation` VALUES ('1387', '75', '6');
INSERT INTO `tfw_relation` VALUES ('1388', '76', '6');
INSERT INTO `tfw_relation` VALUES ('1389', '77', '6');
INSERT INTO `tfw_relation` VALUES ('1390', '78', '6');
INSERT INTO `tfw_relation` VALUES ('1391', '79', '6');
INSERT INTO `tfw_relation` VALUES ('1392', '80', '6');
INSERT INTO `tfw_relation` VALUES ('1393', '81', '6');
INSERT INTO `tfw_relation` VALUES ('1394', '82', '6');
INSERT INTO `tfw_relation` VALUES ('1395', '84', '6');
INSERT INTO `tfw_relation` VALUES ('1396', '85', '6');
INSERT INTO `tfw_relation` VALUES ('1397', '86', '6');
INSERT INTO `tfw_relation` VALUES ('1398', '87', '6');
INSERT INTO `tfw_relation` VALUES ('1399', '83', '6');
INSERT INTO `tfw_relation` VALUES ('1400', '88', '6');
INSERT INTO `tfw_relation` VALUES ('1401', '89', '6');
INSERT INTO `tfw_relation` VALUES ('1402', '90', '6');
INSERT INTO `tfw_relation` VALUES ('1403', '91', '6');
INSERT INTO `tfw_relation` VALUES ('1524', '1', '25');
INSERT INTO `tfw_relation` VALUES ('1525', '62', '25');
INSERT INTO `tfw_relation` VALUES ('1526', '64', '25');
INSERT INTO `tfw_relation` VALUES ('1527', '72', '25');
INSERT INTO `tfw_relation` VALUES ('1528', '73', '25');
INSERT INTO `tfw_relation` VALUES ('1529', '74', '25');
INSERT INTO `tfw_relation` VALUES ('1530', '75', '25');
INSERT INTO `tfw_relation` VALUES ('1531', '76', '25');
INSERT INTO `tfw_relation` VALUES ('1532', '77', '25');
INSERT INTO `tfw_relation` VALUES ('1533', '78', '25');
INSERT INTO `tfw_relation` VALUES ('1534', '79', '25');
INSERT INTO `tfw_relation` VALUES ('1535', '80', '25');
INSERT INTO `tfw_relation` VALUES ('1668', '81', '5');
INSERT INTO `tfw_relation` VALUES ('1669', '82', '5');
INSERT INTO `tfw_relation` VALUES ('1670', '84', '5');
INSERT INTO `tfw_relation` VALUES ('1671', '85', '5');
INSERT INTO `tfw_relation` VALUES ('1672', '86', '5');
INSERT INTO `tfw_relation` VALUES ('1673', '87', '5');
INSERT INTO `tfw_relation` VALUES ('1980', '1', '4');
INSERT INTO `tfw_relation` VALUES ('1981', '2', '4');
INSERT INTO `tfw_relation` VALUES ('1982', '3', '4');
INSERT INTO `tfw_relation` VALUES ('1983', '4', '4');
INSERT INTO `tfw_relation` VALUES ('1984', '5', '4');
INSERT INTO `tfw_relation` VALUES ('1985', '6', '4');
INSERT INTO `tfw_relation` VALUES ('1986', '7', '4');
INSERT INTO `tfw_relation` VALUES ('1987', '81', '4');
INSERT INTO `tfw_relation` VALUES ('1988', '82', '4');
INSERT INTO `tfw_relation` VALUES ('1989', '84', '4');
INSERT INTO `tfw_relation` VALUES ('1990', '85', '4');
INSERT INTO `tfw_relation` VALUES ('1991', '86', '4');
INSERT INTO `tfw_relation` VALUES ('1992', '87', '4');
INSERT INTO `tfw_relation` VALUES ('1993', '83', '4');
INSERT INTO `tfw_relation` VALUES ('1994', '88', '4');
INSERT INTO `tfw_relation` VALUES ('1995', '89', '4');
INSERT INTO `tfw_relation` VALUES ('1996', '90', '4');
INSERT INTO `tfw_relation` VALUES ('1997', '91', '4');
INSERT INTO `tfw_relation` VALUES ('3506', '92', '2');
INSERT INTO `tfw_relation` VALUES ('3507', '105', '2');
INSERT INTO `tfw_relation` VALUES ('3508', '106', '2');
INSERT INTO `tfw_relation` VALUES ('3509', '107', '2');
INSERT INTO `tfw_relation` VALUES ('3510', '108', '2');
INSERT INTO `tfw_relation` VALUES ('3511', '130', '2');
INSERT INTO `tfw_relation` VALUES ('3512', '115', '2');
INSERT INTO `tfw_relation` VALUES ('3513', '116', '2');
INSERT INTO `tfw_relation` VALUES ('3514', '117', '2');
INSERT INTO `tfw_relation` VALUES ('3515', '118', '2');
INSERT INTO `tfw_relation` VALUES ('3516', '120', '2');
INSERT INTO `tfw_relation` VALUES ('3517', '121', '2');
INSERT INTO `tfw_relation` VALUES ('3518', '122', '2');
INSERT INTO `tfw_relation` VALUES ('3519', '123', '2');
INSERT INTO `tfw_relation` VALUES ('3520', '132', '2');
INSERT INTO `tfw_relation` VALUES ('3521', '137', '2');
INSERT INTO `tfw_relation` VALUES ('3522', '125', '2');
INSERT INTO `tfw_relation` VALUES ('3523', '126', '2');
INSERT INTO `tfw_relation` VALUES ('3524', '127', '2');
INSERT INTO `tfw_relation` VALUES ('3525', '128', '2');
INSERT INTO `tfw_relation` VALUES ('3526', '133', '2');
INSERT INTO `tfw_relation` VALUES ('3527', '134', '2');
INSERT INTO `tfw_relation` VALUES ('3528', '135', '2');
INSERT INTO `tfw_relation` VALUES ('3529', '136', '2');
INSERT INTO `tfw_relation` VALUES ('3530', '138', '2');
INSERT INTO `tfw_relation` VALUES ('3531', '139', '2');
INSERT INTO `tfw_relation` VALUES ('3532', '140', '2');
INSERT INTO `tfw_relation` VALUES ('3533', '154', '2');
INSERT INTO `tfw_relation` VALUES ('3534', '142', '2');
INSERT INTO `tfw_relation` VALUES ('3535', '143', '2');
INSERT INTO `tfw_relation` VALUES ('3536', '144', '2');
INSERT INTO `tfw_relation` VALUES ('3537', '145', '2');
INSERT INTO `tfw_relation` VALUES ('3538', '1', '2');
INSERT INTO `tfw_relation` VALUES ('3539', '8', '2');
INSERT INTO `tfw_relation` VALUES ('3540', '9', '2');
INSERT INTO `tfw_relation` VALUES ('3541', '10', '2');
INSERT INTO `tfw_relation` VALUES ('3542', '11', '2');
INSERT INTO `tfw_relation` VALUES ('3543', '12', '2');
INSERT INTO `tfw_relation` VALUES ('3544', '13', '2');
INSERT INTO `tfw_relation` VALUES ('3545', '14', '2');
INSERT INTO `tfw_relation` VALUES ('3546', '15', '2');
INSERT INTO `tfw_relation` VALUES ('3547', '16', '2');
INSERT INTO `tfw_relation` VALUES ('3548', '17', '2');
INSERT INTO `tfw_relation` VALUES ('3549', '18', '2');
INSERT INTO `tfw_relation` VALUES ('3550', '19', '2');
INSERT INTO `tfw_relation` VALUES ('3551', '20', '2');
INSERT INTO `tfw_relation` VALUES ('3552', '21', '2');
INSERT INTO `tfw_relation` VALUES ('3553', '22', '2');
INSERT INTO `tfw_relation` VALUES ('3554', '23', '2');
INSERT INTO `tfw_relation` VALUES ('3555', '24', '2');
INSERT INTO `tfw_relation` VALUES ('3556', '2', '2');
INSERT INTO `tfw_relation` VALUES ('3557', '3', '2');
INSERT INTO `tfw_relation` VALUES ('3558', '4', '2');
INSERT INTO `tfw_relation` VALUES ('3559', '5', '2');
INSERT INTO `tfw_relation` VALUES ('3560', '6', '2');
INSERT INTO `tfw_relation` VALUES ('3561', '7', '2');
INSERT INTO `tfw_relation` VALUES ('3562', '39', '2');
INSERT INTO `tfw_relation` VALUES ('3563', '40', '2');
INSERT INTO `tfw_relation` VALUES ('3564', '41', '2');
INSERT INTO `tfw_relation` VALUES ('3565', '42', '2');
INSERT INTO `tfw_relation` VALUES ('3566', '43', '2');
INSERT INTO `tfw_relation` VALUES ('3567', '44', '2');
INSERT INTO `tfw_relation` VALUES ('3568', '45', '2');
INSERT INTO `tfw_relation` VALUES ('3569', '46', '2');
INSERT INTO `tfw_relation` VALUES ('3570', '47', '2');
INSERT INTO `tfw_relation` VALUES ('3571', '48', '2');
INSERT INTO `tfw_relation` VALUES ('3572', '49', '2');
INSERT INTO `tfw_relation` VALUES ('3573', '81', '2');
INSERT INTO `tfw_relation` VALUES ('3574', '82', '2');
INSERT INTO `tfw_relation` VALUES ('3575', '84', '2');
INSERT INTO `tfw_relation` VALUES ('3576', '85', '2');
INSERT INTO `tfw_relation` VALUES ('3577', '86', '2');
INSERT INTO `tfw_relation` VALUES ('3578', '87', '2');
INSERT INTO `tfw_relation` VALUES ('3579', '83', '2');
INSERT INTO `tfw_relation` VALUES ('3580', '88', '2');
INSERT INTO `tfw_relation` VALUES ('3581', '89', '2');
INSERT INTO `tfw_relation` VALUES ('3582', '90', '2');
INSERT INTO `tfw_relation` VALUES ('3583', '91', '2');
INSERT INTO `tfw_relation` VALUES ('4473', '92', '1');
INSERT INTO `tfw_relation` VALUES ('4474', '105', '1');
INSERT INTO `tfw_relation` VALUES ('4475', '106', '1');
INSERT INTO `tfw_relation` VALUES ('4476', '107', '1');
INSERT INTO `tfw_relation` VALUES ('4477', '108', '1');
INSERT INTO `tfw_relation` VALUES ('4478', '130', '1');
INSERT INTO `tfw_relation` VALUES ('4479', '115', '1');
INSERT INTO `tfw_relation` VALUES ('4480', '116', '1');
INSERT INTO `tfw_relation` VALUES ('4481', '117', '1');
INSERT INTO `tfw_relation` VALUES ('4482', '118', '1');
INSERT INTO `tfw_relation` VALUES ('4483', '120', '1');
INSERT INTO `tfw_relation` VALUES ('4484', '121', '1');
INSERT INTO `tfw_relation` VALUES ('4485', '122', '1');
INSERT INTO `tfw_relation` VALUES ('4486', '123', '1');
INSERT INTO `tfw_relation` VALUES ('4487', '132', '1');
INSERT INTO `tfw_relation` VALUES ('4488', '177', '1');
INSERT INTO `tfw_relation` VALUES ('4489', '125', '1');
INSERT INTO `tfw_relation` VALUES ('4490', '126', '1');
INSERT INTO `tfw_relation` VALUES ('4491', '127', '1');
INSERT INTO `tfw_relation` VALUES ('4492', '128', '1');
INSERT INTO `tfw_relation` VALUES ('4493', '133', '1');
INSERT INTO `tfw_relation` VALUES ('4494', '134', '1');
INSERT INTO `tfw_relation` VALUES ('4495', '135', '1');
INSERT INTO `tfw_relation` VALUES ('4496', '136', '1');
INSERT INTO `tfw_relation` VALUES ('4497', '173', '1');
INSERT INTO `tfw_relation` VALUES ('4498', '176', '1');
INSERT INTO `tfw_relation` VALUES ('4499', '138', '1');
INSERT INTO `tfw_relation` VALUES ('4500', '139', '1');
INSERT INTO `tfw_relation` VALUES ('4501', '140', '1');
INSERT INTO `tfw_relation` VALUES ('4502', '154', '1');
INSERT INTO `tfw_relation` VALUES ('4503', '142', '1');
INSERT INTO `tfw_relation` VALUES ('4504', '143', '1');
INSERT INTO `tfw_relation` VALUES ('4505', '144', '1');
INSERT INTO `tfw_relation` VALUES ('4506', '145', '1');
INSERT INTO `tfw_relation` VALUES ('4507', '169', '1');
INSERT INTO `tfw_relation` VALUES ('4508', '170', '1');
INSERT INTO `tfw_relation` VALUES ('4509', '171', '1');
INSERT INTO `tfw_relation` VALUES ('4510', '172', '1');
INSERT INTO `tfw_relation` VALUES ('4511', '1', '1');
INSERT INTO `tfw_relation` VALUES ('4512', '8', '1');
INSERT INTO `tfw_relation` VALUES ('4513', '9', '1');
INSERT INTO `tfw_relation` VALUES ('4514', '10', '1');
INSERT INTO `tfw_relation` VALUES ('4515', '11', '1');
INSERT INTO `tfw_relation` VALUES ('4516', '12', '1');
INSERT INTO `tfw_relation` VALUES ('4517', '13', '1');
INSERT INTO `tfw_relation` VALUES ('4518', '14', '1');
INSERT INTO `tfw_relation` VALUES ('4519', '15', '1');
INSERT INTO `tfw_relation` VALUES ('4520', '16', '1');
INSERT INTO `tfw_relation` VALUES ('4521', '17', '1');
INSERT INTO `tfw_relation` VALUES ('4522', '18', '1');
INSERT INTO `tfw_relation` VALUES ('4523', '19', '1');
INSERT INTO `tfw_relation` VALUES ('4524', '20', '1');
INSERT INTO `tfw_relation` VALUES ('4525', '21', '1');
INSERT INTO `tfw_relation` VALUES ('4526', '22', '1');
INSERT INTO `tfw_relation` VALUES ('4527', '23', '1');
INSERT INTO `tfw_relation` VALUES ('4528', '24', '1');
INSERT INTO `tfw_relation` VALUES ('4529', '2', '1');
INSERT INTO `tfw_relation` VALUES ('4530', '3', '1');
INSERT INTO `tfw_relation` VALUES ('4531', '4', '1');
INSERT INTO `tfw_relation` VALUES ('4532', '5', '1');
INSERT INTO `tfw_relation` VALUES ('4533', '6', '1');
INSERT INTO `tfw_relation` VALUES ('4534', '7', '1');
INSERT INTO `tfw_relation` VALUES ('4535', '25', '1');
INSERT INTO `tfw_relation` VALUES ('4536', '26', '1');
INSERT INTO `tfw_relation` VALUES ('4537', '27', '1');
INSERT INTO `tfw_relation` VALUES ('4538', '28', '1');
INSERT INTO `tfw_relation` VALUES ('4539', '29', '1');
INSERT INTO `tfw_relation` VALUES ('4540', '30', '1');
INSERT INTO `tfw_relation` VALUES ('4541', '31', '1');
INSERT INTO `tfw_relation` VALUES ('4542', '32', '1');
INSERT INTO `tfw_relation` VALUES ('4543', '33', '1');
INSERT INTO `tfw_relation` VALUES ('4544', '34', '1');
INSERT INTO `tfw_relation` VALUES ('4545', '35', '1');
INSERT INTO `tfw_relation` VALUES ('4546', '36', '1');
INSERT INTO `tfw_relation` VALUES ('4547', '37', '1');
INSERT INTO `tfw_relation` VALUES ('4548', '38', '1');
INSERT INTO `tfw_relation` VALUES ('4549', '39', '1');
INSERT INTO `tfw_relation` VALUES ('4550', '40', '1');
INSERT INTO `tfw_relation` VALUES ('4551', '41', '1');
INSERT INTO `tfw_relation` VALUES ('4552', '42', '1');
INSERT INTO `tfw_relation` VALUES ('4553', '43', '1');
INSERT INTO `tfw_relation` VALUES ('4554', '44', '1');
INSERT INTO `tfw_relation` VALUES ('4555', '45', '1');
INSERT INTO `tfw_relation` VALUES ('4556', '46', '1');
INSERT INTO `tfw_relation` VALUES ('4557', '47', '1');
INSERT INTO `tfw_relation` VALUES ('4558', '48', '1');
INSERT INTO `tfw_relation` VALUES ('4559', '49', '1');
INSERT INTO `tfw_relation` VALUES ('4560', '56', '1');
INSERT INTO `tfw_relation` VALUES ('4561', '57', '1');
INSERT INTO `tfw_relation` VALUES ('4562', '58', '1');
INSERT INTO `tfw_relation` VALUES ('4563', '59', '1');
INSERT INTO `tfw_relation` VALUES ('4564', '60', '1');
INSERT INTO `tfw_relation` VALUES ('4565', '61', '1');
INSERT INTO `tfw_relation` VALUES ('4566', '62', '1');
INSERT INTO `tfw_relation` VALUES ('4567', '63', '1');
INSERT INTO `tfw_relation` VALUES ('4568', '64', '1');
INSERT INTO `tfw_relation` VALUES ('4569', '65', '1');
INSERT INTO `tfw_relation` VALUES ('4570', '81', '1');
INSERT INTO `tfw_relation` VALUES ('4571', '82', '1');
INSERT INTO `tfw_relation` VALUES ('4572', '84', '1');
INSERT INTO `tfw_relation` VALUES ('4573', '85', '1');
INSERT INTO `tfw_relation` VALUES ('4574', '86', '1');
INSERT INTO `tfw_relation` VALUES ('4575', '87', '1');
INSERT INTO `tfw_relation` VALUES ('4576', '83', '1');
INSERT INTO `tfw_relation` VALUES ('4577', '88', '1');
INSERT INTO `tfw_relation` VALUES ('4578', '89', '1');
INSERT INTO `tfw_relation` VALUES ('4579', '90', '1');
INSERT INTO `tfw_relation` VALUES ('4580', '91', '1');
INSERT INTO `tfw_relation` VALUES ('4581', '98', '1');
INSERT INTO `tfw_relation` VALUES ('4582', '99', '1');
INSERT INTO `tfw_relation` VALUES ('4583', '100', '1');
INSERT INTO `tfw_relation` VALUES ('4584', '101', '1');
INSERT INTO `tfw_relation` VALUES ('4585', '102', '1');
INSERT INTO `tfw_relation` VALUES ('4586', '103', '1');
INSERT INTO `tfw_relation` VALUES ('4587', '104', '1');

-- ----------------------------
-- Table structure for tfw_role
-- ----------------------------
DROP TABLE IF EXISTS `tfw_role`;
CREATE TABLE `tfw_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NUM` int(11) DEFAULT NULL,
  `PID` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DEPTID` int(11) DEFAULT NULL,
  `TIPS` varchar(255) DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_role
-- ----------------------------
INSERT INTO `tfw_role` VALUES ('1', '1', null, '超级管理员', '1', 'administrator', '0');
INSERT INTO `tfw_role` VALUES ('2', '1', '1', '管理员', '7', 'admin', '3');

-- ----------------------------
-- Table structure for tfw_role_ext
-- ----------------------------
DROP TABLE IF EXISTS `tfw_role_ext`;
CREATE TABLE `tfw_role_ext` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERID` varchar(255) DEFAULT NULL,
  `ROLEIN` text,
  `ROLEOUT` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_role_ext
-- ----------------------------
INSERT INTO `tfw_role_ext` VALUES ('27', '66', '1,44,49', '45');
INSERT INTO `tfw_role_ext` VALUES ('47', '2', '92,105,106,107,108,130,115,116,117,118,120,121,122,123,132,137,125,126,127,128,133,134,135,136,138,139,140,154,142,143,144,145,1,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,2,3,4,5,6,7,39,40,41,42,43,44,45,46,47,48,49,81,82,84,85,86,87,83,88,89,90,91', '8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24');
INSERT INTO `tfw_role_ext` VALUES ('48', '63', '0', '0');
INSERT INTO `tfw_role_ext` VALUES ('49', '72', '0', '0');
INSERT INTO `tfw_role_ext` VALUES ('50', '74', '0', '0');
INSERT INTO `tfw_role_ext` VALUES ('67', '1', '92,105,106,107,108,130,115,116,117,118,120,121,122,123,132,137,125,126,127,128,133,134,135,136,138,139,140,154,142,143,144,145,155,156,157,158,159,160,161,162,1,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,2,3,4,5,6,7,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,56,57,58,59,60,61,62,63,64,65,81,82,84,85,86,87,83,88,89,90,91,98,99,100,101,102,103,104', '0');
INSERT INTO `tfw_role_ext` VALUES ('87', '168', '92,103,104,105,106,107', '109,110,111,112,113,114,115,116,117,118,119,120,121,122');
INSERT INTO `tfw_role_ext` VALUES ('107', '189', '108,109,110,111,112,113,114,115,116,117,118,119,120,121,122', '0');
INSERT INTO `tfw_role_ext` VALUES ('127', '21', '92,1,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,39,40,41,42,43,98,99,100,101,102,103,104', '0');

-- ----------------------------
-- Table structure for tfw_user
-- ----------------------------
DROP TABLE IF EXISTS `tfw_user`;
CREATE TABLE `tfw_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCOUNT` varchar(45) DEFAULT NULL,
  `PASSWORD` varchar(45) DEFAULT NULL,
  `SALT` varchar(45) DEFAULT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `BIRTHDAY` datetime DEFAULT NULL,
  `SEX` int(11) DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `PHONE` varchar(45) DEFAULT NULL,
  `ROLEID` varchar(255) DEFAULT NULL,
  `DEPTID` int(11) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `CREATETIME` datetime DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tfw_user
-- ----------------------------
INSERT INTO `tfw_user` VALUES ('1', 'admin', '4779e4a9903dfb08f9f877791c516a73', 'admin', '信息科', '2018-01-01 00:00:00', '1', '000@qq.com', '123', '1', '1', '1', '2016-01-29 08:49:53', '32');

-- ----------------------------
-- Table structure for triage
-- ----------------------------
DROP TABLE IF EXISTS `triage`;
CREATE TABLE `triage` (
  `triage_id` int(11) NOT NULL AUTO_INCREMENT,
  `triage_type` int(11) NOT NULL,
  `pager_type` tinyint(4) NOT NULL,
  `call_buffer` int(11) NOT NULL DEFAULT '2',
  `name` varchar(50) NOT NULL,
  `ip` varchar(15) NOT NULL,
  `description` text,
  `create_time` datetime NOT NULL,
  `reorder_type` tinyint(4) NOT NULL,
  `return_flag_step` int(11) NOT NULL DEFAULT '2',
  `triage_pwd` varchar(50) NOT NULL,
  `print_type` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`triage_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of triage
-- ----------------------------


-- ----------------------------
-- Function structure for queryChildren
-- ----------------------------
DROP FUNCTION IF EXISTS `queryChildren`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `queryChildren`(rootid VARCHAR(500),tabname VARCHAR(500)) RETURNS varchar(4000) CHARSET utf8
BEGIN
DECLARE sTemp VARCHAR(4000);
DECLARE sTempChd VARCHAR(4000);
DECLARE icount INTEGER;
DECLARE tname VARCHAR(500);

SET sTemp = '$';
SET sTempChd = rootid;
set tname=tabname;

if INSTR(tname,'tfw_dept')>0 then
select count(1) into icount from tfw_dept where id=sTempChd;
if icount>0 then
WHILE sTempChd is not NULL DO
if sTempChd <> rootid then 
SET sTemp = CONCAT(sTemp,',',sTempChd);
end if;
SELECT group_concat(id) INTO sTempChd FROM tfw_dept where FIND_IN_SET(pid,sTempChd)>0;
END WHILE;
RETURN SUBSTRING(sTemp,3);
ELSE
RETURN null;
end if;
end if;
if INSTR(tname,'tfw_role')>0 then
select count(1) into icount from tfw_role where id=sTempChd;
if icount>0 then
WHILE sTempChd is not NULL DO
if sTempChd <> rootid then 
SET sTemp = CONCAT(sTemp,',',sTempChd);
end if;
SELECT group_concat(id) INTO sTempChd FROM tfw_role where FIND_IN_SET(pid,sTempChd)>0;
END WHILE;
RETURN SUBSTRING(sTemp,3);
ELSE
RETURN null;
end if;
end if;


END
;;
DELIMITER ;
