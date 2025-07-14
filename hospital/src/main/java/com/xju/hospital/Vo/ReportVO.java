package com.xju.hospital.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReportVO {
    // 报告id
    private String reportId;
    // 患者Id
    private String patientId;
    // 病理结果
    private String result;
    // 肺结节位置
    private String location;
    // 直径
    private String diameter;
    // 实性比例
    private String ctr;
    // 危险分类
    private String riskLevel;
    // 体积
    private String volume;
    // 性质
    private String nature;
    // 表征
    private String representation;
    // 恶性概率
    private String malignancy;
    // 中医辨证分型
    private String tcm;
    // 复查时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextTime;
    // 是否需要下次复查
    private String isNext;
    // 设置标签
    private String tagSet;
    // 患者信息
    private String patientName;
    // 患者年龄
    private String patientAge;
    // 患者出生日期
    private String patientBirthday;
    // 患者性别
    private String patientGender;
    // 检查时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;
    // 检查医生
    private String doctorName;
    // 诊断时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date diagnoseTime;
    // 检查项目
    private String checkItem;
    // 检查设备
    private String checkEquipment;
    // 影像层
    private String layer;
    // 恶性分险
    private String malignancyRisk;
    // 肺
    private String lung;
    // 湿润程度
    private String wetDegree;
    // 肺叶
    private String lungLobe;
    // 结节概率
    private String noduleProbability;
    // 肺段
    private String lungSegment;
    // 钙化情况
    private String calcification;
    // 大小
    private String size;
    // 中心密度
    private String centerDensity;
    // AI表(平均密度)
    private String densityAverage;
    // 最大密度
    private String densityMax;
    // AI表(形态类型)
    private String shape;
    // 最小密度
    private String densityMin;
    // 图像所见信息
    private String imageInfo;
    // 影像建议
    private String suggestion;
    // 图片
    private String image;
    // 图片列表
    private List<String>imageList;
    // 标签列表
    private List<String>tagList;
    // 是否删除（逻辑）
    private Integer deleted;
}
