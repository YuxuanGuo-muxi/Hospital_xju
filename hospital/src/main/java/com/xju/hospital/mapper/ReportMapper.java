package com.xju.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xju.hospital.Dto.HistoryDto;
import com.xju.hospital.Dto.PatientPageDto;
import com.xju.hospital.Vo.SearchVo;
import com.xju.hospital.entity.Query;
import com.xju.hospital.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper extends BaseMapper<Report> {
    List<Report>getQuery();

    int updateByDto(Report report);

    List<Report>queryAll(@Param("offset")int offset,@Param("limit")int limit);

    List<Report> queryByTimeAndIdBigger(@Param("patientId")String patientId);

    List<Report> queryByTimeAndId(HistoryDto historyDto);

    List<SearchVo>queryByDetails(Query query);

    Integer queryCount(String patientId);

    List<Report> queryLimit(PatientPageDto page);

    int checkExist(String reportId);
}
