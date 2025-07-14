package com.xju.hospital.Dto;

import lombok.Data;

@Data
public class PatientPageDto {
    private String patientId;
    private Integer pageSize;
    private Integer pageIndex;
}
