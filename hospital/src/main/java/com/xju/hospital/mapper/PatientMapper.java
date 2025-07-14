package com.xju.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xju.hospital.entity.Patient;
import com.xju.hospital.Vo.PatientVO;
import com.xju.hospital.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Patient)表数据库访问层，由MyBatis Generator工具自动生成
 *
 */
@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
    List<PatientVO> query(String id);

    List<PatientVO> queryAsc(String id);

    List<PatientVO> queryDesc(String id);
    // 分页查询
    List<PatientVO> queryLimit(@Param("offset")int offset,@Param("limit")int limit);

    Integer checkIdExists(String patientId);

    List<Patient> getPatients(@Param("offset")int offset,@Param("limit")int limit);

    int updateCancerInfo(Report report);
}

