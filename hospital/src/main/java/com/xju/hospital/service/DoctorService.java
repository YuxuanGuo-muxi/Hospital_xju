package com.xju.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xju.hospital.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (Doctor)表服务接口
 */
public interface DoctorService extends IService<Doctor> {
    List<Doctor>getAll(int offset,int limit);
    boolean deleteByDoctorId(String doctorId);
}
