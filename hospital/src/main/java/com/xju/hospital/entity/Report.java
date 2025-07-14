package com.xju.hospital.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("reports")
public class Report {
    // 报告id
    private String reportId;
    // 患者名字
    private String patientName;
    // 患者生日
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date patientBirthday;
    // 患者年龄
    private String patientAge;
    // 患者性别
    private String patientGender;
    // 患者id
    private String patientId;
    // 患者类型
    private String patientType;
    // 检查时间
    private Date checkTime;
    // 医生名字
    private String doctorName;
    // 诊断时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date diagnoseTime;
    // 检查项目
    private String checkItem;
    // 检查设备
    private String checkEquipment;
    // 是否复查
    private String isNext;
    // 复查时间
    private Date nextTime;
    // 标签设置
    private String tagSet;
    // 诊断建议
    private String diagnosticLogout;
    // 治疗方法
    private String therapyMethod;
    // 疗效
    private String curativeEffect;
    // 是否删除
    private Integer deleted;
}
