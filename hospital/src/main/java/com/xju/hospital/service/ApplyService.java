package com.xju.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xju.hospital.entity.Apply;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ApplyService extends IService<Apply> {
    List<Apply> getLimit(int offset, int limit);

    boolean deleteById(String id);
}


