package com.xju.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xju.hospital.entity.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * (Doctor)表数据库访问层
 *
 */
@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {

    List<Doctor> getAll(@Param("offset")int offset,@Param("limit")int limit);
    // 根据id删除
    Integer deleteByDoctorId(@Param("doctorId")String doctorId);
}

