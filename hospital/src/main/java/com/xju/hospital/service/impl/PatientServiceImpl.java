package com.xju.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xju.hospital.entity.Patient;
import com.xju.hospital.Vo.PatientVO;
import com.xju.hospital.entity.Report;
import com.xju.hospital.mapper.PatientMapper;
import com.xju.hospital.service.PatientService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 *  (Patient)表服务实现类
 *
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {
    @Resource
    private PatientMapper patientMapper;

    @Override
    public List<PatientVO> getInfos(String id) {
        return patientMapper.query(id);
    }
    // 正序
    @Override
    public List<PatientVO> getAsc(String id) {
        return patientMapper.queryAsc(id);
    }
    // 倒序
    @Override
    public List<PatientVO> getDesc(String id) {
        return patientMapper.queryDesc(id);
    }
    // 分页查询
    @Override
    public List<PatientVO> getLimit(int offset, int limit) {
        return patientMapper.queryLimit(offset,limit);
    }

    @Override
    public Integer checkIdExist(String patientId) {
        return patientMapper.checkIdExists(patientId);
    }

    @Override
    public List<Patient> getPatients(int offset, int pageSize) {
        return patientMapper.getPatients(offset, pageSize);
    }

    @Override
    public boolean updateCancerInfo(Report report) {
        return patientMapper.updateCancerInfo(report)>0;
    }


}
