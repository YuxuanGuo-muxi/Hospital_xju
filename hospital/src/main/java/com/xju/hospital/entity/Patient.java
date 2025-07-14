package com.xju.hospital.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("patient")
public class Patient implements Serializable {
    //患者id
    @TableId
    private String patientId;
    // 医生编号
    private String doctorId;
    // 身份证号
    private String admissionNumber;
    // 门诊号
    private String clinicNo;
    // 患者姓名
    private String patientName;
    // 病例来源
    private String source;
    // 患者性别
    private String patientGender;
    // 患者年龄
    private String patientAge;
    // 民族
    private String patientGroup;
    // 患者工作
    private String patientWork;
    // 患者类型
    private String patientType;
    // 患者学历
    private String patientEduLevel;
    // 患者生日
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String patientBirthday;
    // 婚姻状况
    private String patientMarriage;
    // 是否怀孕
    private String patientPregnancy;
    // 患者住址
    private String patientAddress;
    // 患者电话
    private String patientPhone;
    // 恶性肿瘤家族
    private String patientMalignant;
    // 是否药物过敏
    private String patientAllergy;
    // 过敏药物名称
    private String drugName;
    // 是否吸烟
    private String isSmoke;
    // 吸烟方式
    private String smokingType;
    // 吸烟年限
    private Integer smokingYears;
    // 吸烟量
    private Integer smokingAmount;
    // 是否戒烟
    private String isQuit;
    // 戒烟年限
    private Integer quitYears;
    // 既往病史
    private String medicalHistory;
    // 登记时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createTime;


}
