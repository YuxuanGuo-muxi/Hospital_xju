package com.xju.hospital.Dto;

import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xju.hospital.entity.PulmonaryNodule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReportDto {
    // 报告id
    private String reportId;
    // 患者Id
    private String patientId;
    // 患者名字
    private String patientName;
    // 患者生日
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date patientBirthday;
    // 患者年龄
    private String patientAge;
    // 患者性别
    private String patientGender;
    // 检查时间
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextTime;
    // 标签列表
    private JSONArray tagList;
    // 结节表
    private JSONArray noduleList;
    // 修改时候的Image属性
    private String imageThird;
    // 是否删除（逻辑）
    private Integer deleted;
    // 诊断建议
    private String diagnosticLogout;
    // 治疗方法
    private String therapyMethod;
    // 疗效
    private String curativeEffect;
}
