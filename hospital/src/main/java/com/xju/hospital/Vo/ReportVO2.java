package com.xju.hospital.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ReportVO2 {
    private Integer seconds;
    private String doctorName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextTime;
    private String suggestion;
    private String reportId;
    private String patientType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date diagnoseTime;
}
