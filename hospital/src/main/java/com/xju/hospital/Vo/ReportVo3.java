package com.xju.hospital.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ReportVo3 {
    private String reportId;
    private String doctorId;
    private Integer seconds;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date diagnoseTime;
    private String diagnosticLogout;
    private String therapyMethod;
    private String curativeEffect;

}
