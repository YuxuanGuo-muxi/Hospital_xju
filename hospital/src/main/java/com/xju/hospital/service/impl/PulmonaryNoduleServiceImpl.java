package com.xju.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xju.hospital.entity.PulmonaryNodule;
import com.xju.hospital.mapper.PulmonaryNoduleMapper;
import com.xju.hospital.service.PulmonaryNoduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PulmonaryNoduleServiceImpl extends ServiceImpl<PulmonaryNoduleMapper, PulmonaryNodule> implements PulmonaryNoduleService {
    @Autowired PulmonaryNoduleMapper pulmonaryNoduleMapper;
    @Override
    public Integer checkIdExists(String nodeId) {
        return pulmonaryNoduleMapper.checkIdExists(nodeId);
    }

    @Override
    public boolean deleteNodulesById(String reportId) {
        return pulmonaryNoduleMapper.deleteByReportId(reportId)>0;
    }
}
