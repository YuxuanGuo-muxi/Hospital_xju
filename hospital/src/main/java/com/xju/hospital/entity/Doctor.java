package com.xju.hospital.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("doctor")
public class Doctor {
    // 医生id
    private String doctorId;
    // 医生姓名
    private String doctorName;
    // 医生年龄
    private String doctorAge;
    // 医生科室
    private String department;
    // 职称
    private String doctorTitle;
    // 医生密码
    private String password;
    // 权限
    private Integer power;
}
