package com.xju.hospital.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientVO {
    // 患者id;
    private String patientId;
    // 患者姓名
    private String patientName;
    // 患者标签
    private String tagName;
    // 下次检查时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextTime;
    // 患者结果
    private String result;
    // 患者表征
    private String representation;
    // 患者图片
    private String image;
}
