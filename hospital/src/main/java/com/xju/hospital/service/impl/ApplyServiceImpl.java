package com.xju.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xju.hospital.entity.Apply;
import com.xju.hospital.mapper.ApplyMapper;
import com.xju.hospital.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {
    @Autowired
    private ApplyMapper applyMapper;
    // 分页查询
    @Override
    public List<Apply> getLimit(int offset, int limit) {
        return applyMapper.getLimit(offset,limit);
    }
    // 根据id删除
    @Override
    public boolean deleteById(String id) {
        return applyMapper.deleteById(id)>0;
    }
}
