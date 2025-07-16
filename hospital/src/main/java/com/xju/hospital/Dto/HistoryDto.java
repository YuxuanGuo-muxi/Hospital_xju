package com.xju.hospital.Dto;

import lombok.Data;

//增加实体
@Data
public class HistoryDto {
    private String patientId;
    private Integer pageIndex;
    private Integer pageSize;
}
