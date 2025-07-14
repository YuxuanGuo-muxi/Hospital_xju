/*
 Navicat Premium Dump SQL

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : hospital

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 10/07/2025 14:37:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for apply
-- ----------------------------
DROP TABLE IF EXISTS `apply`;
CREATE TABLE `apply`  (
  `apply_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`apply_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of apply
-- ----------------------------
INSERT INTO `apply` VALUES ('user1', 'pass1', 'Comment 1');
INSERT INTO `apply` VALUES ('user10', 'pass10', 'Comment 10');
INSERT INTO `apply` VALUES ('user2', 'pass2', 'Comment 2');
INSERT INTO `apply` VALUES ('user3', 'pass3', 'Comment 3');
INSERT INTO `apply` VALUES ('user4', 'pass4', 'Comment 4');
INSERT INTO `apply` VALUES ('user5', 'pass5', 'Comment 5');
INSERT INTO `apply` VALUES ('user6', 'pass6', 'Comment 6');
INSERT INTO `apply` VALUES ('user7', 'pass7', 'Comment 7');
INSERT INTO `apply` VALUES ('user8', 'pass8', 'Comment 8');
INSERT INTO `apply` VALUES ('user9', 'pass9', 'Comment 9');

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor`  (
  `doctor_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `doctor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `doctor_age` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `doctor_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `power` int NULL DEFAULT 0,
  PRIMARY KEY (`doctor_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;
  

-- ----------------------------
-- Records of doctor
-- ----------------------------


INSERT INTO `doctor` VALUES ('admin', 'admin', '1', '1', '1', '123', 1);
INSERT INTO `doctor` VALUES ('dr1', 'DoctorA', '40', 'Cardiology', 'Specialist', 'dr1', 0);
INSERT INTO `doctor` VALUES ('dr2', 'DoctorB', '45', 'Neurology', 'Specialist', 'dr2', 0);
INSERT INTO `doctor` VALUES ('dr3', 'DoctorC', '35', 'Dermatology', 'Specialist', 'dr3', 0);
INSERT INTO `doctor` VALUES ('dr4', 'DoctorD', '50', 'Oncology', 'Specialist', 'dr4', 0);
INSERT INTO `doctor` VALUES ('dr5', 'DoctorE', '42', 'Pediatrics', 'Specialist', 'dr5', 0);

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient`  (
  `patient_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `admission_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `clinic_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '住院号',
  `doctor_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `patient_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `source` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '病例来源',
  `patient_gender` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `patient_age` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `patient_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '患者类型',
  `patient_birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `patient_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '民族',
  `patient_work` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职业',
  `patient_edu_level` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教育程度',
  `patient_marriage` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '婚姻状况',
  `patient_pregnancy` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '妊娠史',
  `patient_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `patient_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `patient_malignant` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '恶性肿瘤家族史',
  `patient_allergy` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否药物过敏',
  `drug_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '药物名称',
  `is_smoke` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否吸烟',
  `smoking_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '吸烟方式',
  `smoking_years` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `smoking_amount` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '吸烟量',
  `is_quit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否戒烟',
  `quit_years` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '戒烟年限',
  `medical_history` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '既往病史',
  `create_time` date NULL DEFAULT NULL,
  PRIMARY KEY (`patient_id`) USING BTREE,
  INDEX `_doctor_patient`(`doctor_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of patient
-- ----------------------------
INSERT INTO `patient` VALUES ('1873322713225908225', '12', NULL, 'dr1', '小敏', '住院号', '男', '39', '肺结节转肺癌', NULL, '汉族', '知识分子', '大专、本科及以上', '未婚', '无孕产', '西北路', '31231231231', '无', '否', NULL, '是', '被动', '10', NULL, NULL, NULL, '支气管哮喘', '2025-7-29');
INSERT INTO `patient` VALUES ('1873339731892363265', '23', NULL, 'dr1', '小南瓜', '住院号', '男', '29', '肺结节', NULL, '汉族', '农民', '大专、本科及以上', '已婚', '有孕产', '西北路22号', '1231231231', '', '是', '阿莫西林', '是', '主动', '12', '6', '是', '3', '支气管哮喘', '2025-7-29');
INSERT INTO `patient` VALUES ('1873351434352844802', '22', NULL, 'dr2', '南瓜头', '住院号', '男', '29', '肺结节', NULL, '汉族', '管理人员', '大专、本科及以上', '未婚', '无孕产', '西北路223', '12123', '无', '是', '头孢', '是', '主动', '9', '9', '是', '1', '慢支', '2025-7-29');
INSERT INTO `patient` VALUES ('1873352330121961474', '26', NULL, 'dr2', '筱筱', '住院号', '女', '26', '肺结节', NULL, '汉族', '知识分子', '大专、本科及以上', '已婚', '无孕产', '西北路226', '13234', '无', '否', NULL, '否', NULL, NULL, NULL, NULL, NULL, '支气管哮喘', '2025-7-29');
INSERT INTO `patient` VALUES ('1873352681277480961', '36', NULL, 'dr3', '北鸣', '住院号', '男', '33', '肺癌', NULL, '汉族', '矿工', '初中、高中及中专', '已婚', '无孕产', '西北路113', '142365', '有', '否', NULL, '是', '主动', '12', '10', '是', '3', '支气管哮喘', '2025-7-29');
INSERT INTO `patient` VALUES ('1875016080649457665', '122', NULL, 'dr1', '1', '住院号', '男', '1', '肺结节', NULL, '1', '矿工', '初中、高中及中专', '已婚', '有孕产', '1', '1', '', '是', '1', '是', '主动', '1', '1', '是', '1', '慢支', '2025-7-29');

-- ----------------------------
-- Table structure for patient_tags
-- ----------------------------
DROP TABLE IF EXISTS `patient_tags`;
CREATE TABLE `patient_tags`  (
  `tag_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `patient_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of patient_tags
-- ----------------------------

-- ----------------------------
-- Table structure for pulmonary_nodule
-- ----------------------------
DROP TABLE IF EXISTS `pulmonary_nodule`;
CREATE TABLE `pulmonary_nodule`  (
  `nodule_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `report_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '外键（一个随访报告对应多个肺结节信息）',
  `result` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT图像信息表（病理结果）',
  `risk_level` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT图像信息表（危险分类）',
  `ctr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT图像信息表（实性比例）',
  `representation` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT图像信息表（表征）',
  `tcm` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT图像信息表（中医辩证分型）',
  `malignancy` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT图像信息表（恶性概率）',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'AI影像表现',
  `layer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '影像层',
  `malignancy_risk` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '恶性风险',
  `lung` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '肺',
  `wet_degree` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '湿润程度',
  `lung_lobe` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '肺叶',
  `nodule_probability` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结节概率',
  `lung_segment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '肺段',
  `calcification` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '钙化情况',
  `size` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大小',
  `center_density` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中心密度',
  `volume` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '体积',
  `density_average` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平均密度',
  `diameter` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准直径',
  `density_max` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最大密度',
  `shape` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '形状类型',
  `density_min` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最小密度',
  `post_op_diagnosis` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '术后诊断建议',
  `suggestion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '影像建议',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` int NULL DEFAULT 0,
  PRIMARY KEY (`nodule_id`, `report_id`) USING BTREE,
  INDEX `_patient_reports`(`report_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pulmonary_nodule
-- ----------------------------
INSERT INTO `pulmonary_nodule` VALUES ('03d7fa06-7aad-41e3-8254-1856149eb1db', 'd3a5f9ed-44f3-4509-bf94-4febb86e96ef', '良性', '低危', '123', '毛刺', '肺脾气虚', '3123', '', '123', '磨玻璃', '231', '1', '1', '1', '1', '11', '1', '1', '1', '1', '1', '11', '1', '1', '12312', '123123213', '213', 0);
INSERT INTO `pulmonary_nodule` VALUES ('4000fe0b-75ea-4ef8-b35e-75c1c61a0b10', 'a9a3a031-9b5e-43ed-b7c3-167bcc71a046', '良性', '低危', '12%', '分叶', '肝郁气滞', '12', 'https://sky-msz.oss-cn-hangzhou.aliyuncs.com/jlpto-20241229200759.jpg;', '21', '实性', '21', '12', '21', '21', '21', '21', '2113', '121', '12', '12', '1221', '12', '21', '21', '2131213', '21312123', '', 0);
INSERT INTO `pulmonary_nodule` VALUES ('415e96cc-718c-4abe-9f5e-f19bac90a6be', '2e0b2adf-d451-4be6-a55e-25d9ac374129', '良性', '低危', '2%', '毛刺', '痰浊壅肺', '2', '', '2', '实性', '2', '2', '3', '1', '2', '2', '5', '4', '5', '4', '5', '4', '4', '2', '2444', '24524', '2424', 0);
INSERT INTO `pulmonary_nodule` VALUES ('48807599-3589-4ef7-8d8e-f1dab6b0c595', '53968c01-7466-42f8-9c6e-cd0000beb77c', '恶性', '中危', '16%', '血管征', '寒凝阻络', '16', '', '16', '混合性结节', '16', '16', '16', '16', '15', '15', '30', '30', '34', '34', '35', '35', '36', '35', '2424', '4244', '2424', 0);
INSERT INTO `pulmonary_nodule` VALUES ('95f003c7-e476-455e-b59e-c746d0b65770', 'd3a5f9ed-44f3-4509-bf94-4febb86e96ef', '良性', '低危', '12%', '分叶', '肺脾气虚', '231', 'https://sky-msz.oss-cn-hangzhou.aliyuncs.com/kgyfq-20241229201228.jpg;', '123', '实性', '123', '3123', '123', '3', '3123', '123', '1231', '3123', '123123', '23123', '1231', '312312', '331', '121', '23123', '1231', '12312', 0);
INSERT INTO `pulmonary_nodule` VALUES ('a4bcb18b-f006-47c2-bf5f-e3a745dfd1b3', '0edaa5eb-3ed7-4036-8557-f3bafc7527d1', '恶性', '中危', '15%', '血管征', '寒凝阻络', '18', '', '16', '磨玻璃', '15', '12', '13', '13', '14', '13', '30', '32', '34', '32', '33', '23', '34', '22', '131', '131231', '3131', 0);
INSERT INTO `pulmonary_nodule` VALUES ('f630b88a-66f4-4f23-ae41-9b40f89e0f73', '45ba91be-9d7f-4735-b965-27a816ab0f47', '良性', '低危', '2%', '毛刺', '痰浊壅肺', '2', '', '2', '实性', '2', '2', '2', '2', '2', '2', '6', '6', '6', '6', '6', '6', '6', '6', '5454', '5454', '54545', 0);

-- ----------------------------
-- Table structure for reports
-- ----------------------------
DROP TABLE IF EXISTS `reports`;
CREATE TABLE `reports`  (
  `report_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `patient_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT基本信息表',
  `patient_birthday` date NULL DEFAULT NULL,
  `patient_age` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `patient_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键（一个患者对应多个随访记录）',
  `patient_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '患者类型',
  `patient_gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT基本信息表（性别）',
  `check_time` date NULL DEFAULT NULL COMMENT 'CT基本信息表（检查日期）',
  `doctor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `diagnose_time` date NULL DEFAULT NULL COMMENT 'CT基本信息表（诊断日期）',
  `check_item` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT基本信息表（检查项目）',
  `check_equipment` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CT基本信息表（检查设备）',
  `is_next` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否复查',
  `next_time` date NULL DEFAULT NULL COMMENT '复查时间',
  `tag_set` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签设置',
  `diagnostic_logout` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '诊断建议',
  `therapy_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '治疗方法',
  `curative_effect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` int NULL DEFAULT 0,
  PRIMARY KEY (`report_id`) USING BTREE,
  INDEX `_patient_reports`(`patient_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of reports
-- ----------------------------
INSERT INTO `reports` VALUES ('0edaa5eb-3ed7-4036-8557-f3bafc7527d1', '筱筱', NULL, '26', '26', '肺结节', '女', '2025-7-29', 'dr2', '2025-7-29', 'CT', 'CT', '是', '2025-7-29', '有研究价值,', NULL, NULL, NULL, 0);
INSERT INTO `reports` VALUES ('2e0b2adf-d451-4be6-a55e-25d9ac374129', '南瓜头', NULL, '29', '22', '肺结节', '男', '2025-7-299', '', '2025-7-29', 'CT', 'CT', '是', '2025-7-29', '轻微,', NULL, NULL, NULL, 0);
INSERT INTO `reports` VALUES ('30713fec-fc6d-4ffc-9a4c-edc3200bfdda', '小敏', NULL, '39', '12', '肺癌', '男', NULL, 'dr1', '2025-7-29', NULL, NULL, NULL, NULL, NULL, '242452452452424', '化疗', '245245245242', 0);
INSERT INTO `reports` VALUES ('45ba91be-9d7f-4735-b965-27a816ab0f47', '南瓜头', NULL, '29', '22', '肺结节', '男', '2025-7-29', 'dr2', '2025-7-29', 'CT', 'CT', '是', '2025-7-29', '有研究价值,严重,轻微,', NULL, NULL, NULL, 0);
INSERT INTO `reports` VALUES ('53968c01-7466-42f8-9c6e-cd0000beb77c', '筱筱', NULL, '26', '26', '肺结节', '女', '2025-7-29', 'dr2', '2025-7-29', 'CT', 'CT', '是', '2025-7-29', '严重,', NULL, NULL, NULL, 0);
INSERT INTO `reports` VALUES ('5b216141-7310-44f4-85a5-18433756110c', '北鸣', NULL, '33', '36', '肺癌', '男', NULL, 'dr3', '2025-7-29', NULL, NULL, NULL, NULL, NULL, '4224524524', '免疫治疗', '24524', 0);
INSERT INTO `reports` VALUES ('a9a3a031-9b5e-43ed-b7c3-167bcc71a046', '小南瓜', '2025-7-29', '29', '23', '肺结节', '男', '2025-7-29', 'dr1', '2025-7-29', 'CT', 'CT', '是', '2025-7-29', '有研究价值,', NULL, NULL, NULL, 0);
INSERT INTO `reports` VALUES ('d3a5f9ed-44f3-4509-bf94-4febb86e96ef', '小南瓜', NULL, '29', '23', '肺结节', '男', '2025-7-29', '', '2025-7-29', 'CT', 'CT', '是', '2025-7-29', '轻微,', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for search_ing
-- ----------------------------
DROP TABLE IF EXISTS `search_ing`;
CREATE TABLE `search_ing`  (
  `search_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `search_item` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '搜索项的字段名称',
  `search_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '搜索项的名称',
  PRIMARY KEY (`search_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of search_ing
-- ----------------------------
INSERT INTO `search_ing` VALUES ('1', '23', '小南瓜的身份证');

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags`  (
  `tag_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tag_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tag_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `count` int NULL DEFAULT NULL,
  `is_disabled` int NULL DEFAULT 0,
  PRIMARY KEY (`tag_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tags
-- ----------------------------
INSERT INTO `tags` VALUES ('7b1846e8-cb70-4617-b4f8-af32db0a5335', '有研究价值', '有研究价值', 0, 0);
INSERT INTO `tags` VALUES ('7fb59180-370e-476a-9a86-1f34aadcd44a', '已使用新研制的中药', '已使用新研制的中药', 0, 0);
INSERT INTO `tags` VALUES ('c923ebc7-80c9-4ddf-9942-ef20f144160d', '严重', '严重', 0, 0);
INSERT INTO `tags` VALUES ('e6b9c098-5a97-4d18-9961-d868655d72ca', '轻微', '轻微', 0, 0);
INSERT INTO `tags` VALUES ('ec47500f-341a-4b99-9b86-1077163f7f97', '12', '12', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
