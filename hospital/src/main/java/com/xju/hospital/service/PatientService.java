package com.xju.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xju.hospital.entity.Patient;
import com.xju.hospital.Vo.PatientVO;
import com.xju.hospital.entity.Report;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  (Patient)表服务接口
 *
 */
public interface PatientService extends IService<Patient> {

    List<PatientVO>getInfos(String id);

    List<PatientVO> getAsc(String id);

    List<PatientVO> getDesc(String id);

    List<PatientVO> getLimit(int offset,int limit);

    Integer checkIdExist(String patientId);

    List<Patient>getPatients(int offset,int pageSize);

    boolean updateCancerInfo(Report report);

}
