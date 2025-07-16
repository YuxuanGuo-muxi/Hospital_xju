package com.xju.hospital.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("pulmonary_nodule")
public class PulmonaryNodule {
    // 结节id
    private String noduleId;
    // 报告id
    private String reportId;
    //结果
    private String result;
    // 风险等级
    private String riskLevel;
    // ctr
    private String ctr;
    // 表征
    private String representation;
    // 中医辨证分型
    private String tcm;
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
    // 中心密度
    private String centerDensity;
    // 体积
    private String volume;
    // 平均密度
    private String densityAverage;
    // 标准直径
    private String diameter;
    // 最大密度
    private String densityMax;
    // 形状类型
    private String shape;
    // 最小密度
    private String densityMin;
    // 术后诊断建议
    private String postOpDiagnosis;
    // 影响建议
    private String suggestion;
    // 备注
    private String remark;
    // 是否删除
    private Integer deleted;
}
