package com.xju.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xju.hospital.mapper.DoctorMapper;
import com.xju.hospital.entity.Doctor;
import com.xju.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Doctor)表服务实现类
 *
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {
    @Autowired
    private DoctorMapper doctorMapper;

    /**
     * 查询医生的分页信息
     * @param offset 页码
     * @param limit 一页多少条数据
     * @return 结果
     */
    @Override
    public List<Doctor> getAll(int offset, int limit) {
        return doctorMapper.getAll(offset,limit);
    }

    /**
     * 根据id删除医生
     * @param doctorId 医生
     * @return 是否删除成功
     */
    @Override
    public boolean deleteByDoctorId(String doctorId) {
        return doctorMapper.deleteByDoctorId(doctorId)>0;
    }
}
