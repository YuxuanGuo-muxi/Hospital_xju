package com.xju.hospital.entity;

import cn.hutool.json.JSONArray;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class Query {
    // 医生id
    private String doctorId;
    // 标签
    private String tags;
    // 患者名字
    private String patientName;
    // 页码
    private Integer pageIndex;
    // 页面大小
    private Integer pageSize;
    // 排序方式
    private Integer sortMode;
    // 中心密度
    private String centerDensity;
    // 最大湿度
    private String densityMax;
    // 最小湿度
    private String densityMin;
    // 平均密度
    private String densityAverage;
    // 表征
    private String representation;
    // tcm
    private String tcm;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date patientBirthday;
    // 患者年龄
    private Integer patientAge;
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
    // 结节id
    private String noduleId;
    //结果
    private String result;
    // 风险等级
    private String riskLevel;
    // ctr
    private String ctr;
    // 恶性概率
    private String malignancy;
    // AI影像表现
    private String image;
    // 影像层
    private String layer;
    // 恶性风险
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
    // 体积
    private String volume;
    // 标准直径
    private String diameter;
    // 形状类型
    private String shape;
    // 术后诊断建议
    private String postOpDiagnosis;
    // 影响建议
    private String suggestion;
    // 备注
    private String remark;
}
