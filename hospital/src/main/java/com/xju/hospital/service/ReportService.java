package com.xju.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xju.hospital.Dto.HistoryDto;
import com.xju.hospital.Dto.PatientPageDto;
import com.xju.hospital.Vo.SearchVo;
import com.xju.hospital.entity.Query;
import com.xju.hospital.entity.Report;

import java.util.List;

public interface ReportService extends IService<Report> {
    List<Report>queryByTime();
    boolean updateReport(Report report);
    List<Report>queryAll(int offset,int limit);
    List<Report>queryByTimeAndId(HistoryDto historyDto);
    List<Report>queryByTimAndIdBigger(String patientId);
    List<SearchVo>queryByDetails(Query query);
    List<Report>getLimit(PatientPageDto page);
    Integer checkExist(String reportId);

}
