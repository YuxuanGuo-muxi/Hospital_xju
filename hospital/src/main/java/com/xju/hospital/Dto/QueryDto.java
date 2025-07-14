package com.xju.hospital.Dto;

import cn.hutool.json.JSONArray;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xju.hospital.entity.TagSearch;
import lombok.Data;

import java.util.Date;

@Data
public class QueryDto {
    private String doctorId;
    private JSONArray tags;
    private String patientName;
    private Integer pageIndex;
    private Integer pageSize;
    private Integer sortMode;
    private TagSearch tagSearch;
}
