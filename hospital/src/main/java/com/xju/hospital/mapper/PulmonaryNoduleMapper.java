package com.xju.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xju.hospital.entity.PulmonaryNodule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PulmonaryNoduleMapper extends BaseMapper<PulmonaryNodule> {
    Integer checkIdExists(String nodeId);

    int deleteByReportId(String reportId);
}
