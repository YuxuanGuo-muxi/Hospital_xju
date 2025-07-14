package com.xju.hospital.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 返回连表查询的结果对象(结节，报告)
 */
 
@Data
public class SearchVo {
    private String reportId;
    // 患者信息
    private String patientName;
    // 患者出生日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date patientBirthday;
    // 患者年龄
    private String patientAge;
    // 患者Id
    private String patientId;
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
    // 是否需要下次复查
    private String isNext;
    // 复查时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextTime;
    // 设置标签
    private String tagSet;
    // 转换为标签列表
    private List<String>tagList;
    // 结节列表
    private List<PulmonaryNoduleVo> noduleList;
}
