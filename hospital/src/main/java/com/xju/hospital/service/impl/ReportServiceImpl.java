package com.xju.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xju.hospital.Dto.HistoryDto;
import com.xju.hospital.Dto.PatientPageDto;
import com.xju.hospital.Vo.SearchVo;
import com.xju.hospital.entity.Query;
import com.xju.hospital.entity.Report;
import com.xju.hospital.mapper.ReportMapper;
import com.xju.hospital.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<Report> queryByTime() {
        return reportMapper.getQuery();
    }


    @Override
    public boolean updateReport(Report report) {
        return reportMapper.updateByDto(report)>0;
    }

    @Override
    public List<Report> queryAll(int offset, int limit) {
        return reportMapper.queryAll(offset,limit);
    }

    @Override
    public List<Report> queryByTimeAndId(HistoryDto historyDto) {
        return reportMapper.queryByTimeAndId(historyDto);
    }

    @Override
    public List<Report> queryByTimAndIdBigger(String patientId) {
        return reportMapper.queryByTimeAndIdBigger(patientId);
    }

    @Override
    public List<SearchVo> queryByDetails(Query query) {
        return reportMapper.queryByDetails(query);
    }

    @Override
    public List<Report> getLimit(PatientPageDto page) {
        return reportMapper.queryLimit(page);
    }

    @Override
    public Integer checkExist(String reportId) {
        return reportMapper.checkExist(reportId);
    }
}
