package com.xju.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xju.hospital.entity.PulmonaryNodule;

public interface PulmonaryNoduleService extends IService<PulmonaryNodule> {
    Integer checkIdExists(String nodeId);

    boolean deleteNodulesById(String reportId);
}
