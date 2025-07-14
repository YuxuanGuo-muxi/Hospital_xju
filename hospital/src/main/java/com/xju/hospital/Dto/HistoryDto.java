package com.xju.hospital.Dto;

import lombok.Data;

@Data
public class HistoryDto {
    private String patientId;
    private Integer pageIndex;
    private Integer pageSize;
}
