package com.xju.hospital.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.xju.hospital.Dto.*;
import com.xju.hospital.Vo.*;
import com.xju.hospital.entity.*;
import com.xju.hospital.service.PatientService;
import com.xju.hospital.service.PulmonaryNoduleService;
import com.xju.hospital.service.ReportService;
import com.xju.hospital.util.AliOSSUtils;
import com.xju.hospital.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

@RestController
@RequestMapping("/api/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Autowired
    private PatientService patientService;
    @Autowired
    private PulmonaryNoduleService pulmonaryNoduleService;
    private static final String PATIENT_TYPE = "肺结节";
    /**
     * 数据添加
     *
     * @param reportDto 封装数据
     */
    @PostMapping("/add")
    @Transactional
    public Result insertReportAndNodules(@RequestBody ReportDto reportDto) {
        // 属性拷贝
        Report report = new Report();
        BeanUtils.copyProperties(reportDto, report);
        log.info(report.toString());

        // 转换标签列表为字符串
        String tags = convertJSONArrayToString(reportDto.getTagList());
        report.setTagSet(tags);

        // 生成并设置唯一的报告ID
        String reportId = generateUniqueUUID(reportService::checkExist);
        report.setReportId(reportId);

        // 查询患者并设置患者类型
        Patient patient = fetchPatient(reportDto.getPatientId());
        if (patient == null) {
            return Result.error("患者不存在");
        }
        report.setPatientType(patient.getPatientType());

        // 插入报告
        int insert = reportService.getBaseMapper().insert(report);
        if (insert <= 0) {
            return Result.error("报告插入失败");
        }

        // 插入结节
        if (!insertNodules(reportDto.getNoduleList(), reportId)) {
            return Result.error("结节插入失败");
        }

        return Result.ok("插入成功");
    }

    private String convertJSONArrayToString(JSONArray jsonArray) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                Object value = jsonArray.get(i);
                String tagValue = value.toString();
                result.append(tagValue).append(",");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    private String generateUniqueUUID(Function<String, Integer> checkExistFunction) {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (checkExistFunction.apply(uuid.toString()) > 0);
        return uuid.toString();
    }

    private Patient fetchPatient(String patientId) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("admission_number", patientId);
        return patientService.getBaseMapper().selectOne(wrapper);
    }

    private boolean insertNodules(JSONArray nodules, String reportId) {
        for (int i = 0; i < nodules.size(); i++) {
            Object value = nodules.get(i);
            Gson gson = new Gson();
            PulmonaryNodule pulmonaryNodule = gson.fromJson(value.toString(), PulmonaryNodule.class);
            log.info(pulmonaryNodule.toString());
            pulmonaryNodule.setReportId(reportId);
            String noduleId = generateUniqueUUID(pulmonaryNoduleService::checkIdExists);
            pulmonaryNodule.setNoduleId(noduleId);

            int insert = pulmonaryNoduleService.getBaseMapper().insert(pulmonaryNodule);
            if (insert <= 0) {
                return false;
            }
        }
        return true;
    }

    // 上传图片
    @PostMapping("/upload")
    public String handleUserUpload(@RequestBody MultipartFile file) throws IOException {
        return aliOSSUtils.upload(file);
    }

    /**
     * 得到单个报告的信息
     *
     * @param id 报告id
     * @return 是否成功
     */
    @GetMapping("/get/{id}")
    public Result getByReportId(@PathVariable("id") String id) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id", id);
        Report report = reportService.getBaseMapper().selectOne(queryWrapper);
        return Result.ok(report);
    }

    /**
     * 得到单个报告的信息
     *
     * @param id 报告id
     * @return 是否成功
     */
    @GetMapping("/getReport/{id}")
    public Result getByReport(@PathVariable("id") String id) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id", id);
        Report report = reportService.getBaseMapper().selectOne(queryWrapper);
        QueryWrapper<PulmonaryNodule> qW = new QueryWrapper<>();
        qW.eq("report_id", id);
        List<PulmonaryNodule> nodules = pulmonaryNoduleService.getBaseMapper().selectList(qW);
        List<PulmonaryNoduleVo> noduleVos = new ArrayList<>();
        for (int i = 0; i < nodules.size(); i++) {
            // 初始化
            PulmonaryNoduleVo pulmonaryNoduleVo = new PulmonaryNoduleVo();
            // 拷贝属性
            BeanUtils.copyProperties(nodules.get(i), pulmonaryNoduleVo);
            String[] images = nodules.get(i).getImage().split(";");
            pulmonaryNoduleVo.setImageList(Arrays.asList(images));
            noduleVos.add(pulmonaryNoduleVo);
        }
        SearchVo searchVo = new SearchVo();
        BeanUtils.copyProperties(report, searchVo);
        searchVo.setNoduleList(noduleVos);
        return Result.ok(searchVo);
    }

    /**
     * 根据id查询记录
     *
     * @return 查询记录
     */
    @GetMapping("/getAllRecords")
    public Result getByDoctorId() {
        log.info("查询七天内的记录");
        List<ReportVO> reports = new ArrayList<>();
        for (int i = 0; i < reportService.queryByTime().size(); i++) {
            ReportVO reportVO = new ReportVO();
            BeanUtils.copyProperties(reportService.queryByTime().get(i), reportVO);
            log.info(reportService.queryByTime().get(i).toString());
            Date nextTime = reportService.queryByTime().get(i).getNextTime();
            LocalDate localNextTime = nextTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // 添加一天
            localNextTime = localNextTime.plusDays(1);
            // 将 LocalDate 转换回 Date
            nextTime = java.sql.Date.valueOf(localNextTime);
            reportVO.setNextTime(nextTime);
            reports.add(reportVO);
        }
        return Result.ok(reports);
    }

    /**
     * 查询单个病人的报告
     *
     * @param queryDto 条件查询
     * @return 是否成功
     */
    @PostMapping("/search")
    public Result getReportByDoctorId(@RequestBody QueryDto queryDto) {
        // 获取到要搜索的字段
        TagSearch tagSearch = null;
        if(queryDto.getTagSearch()!=null){
            tagSearch = queryDto.getTagSearch();
        };
        Query query = new Query();
        if(tagSearch!=null){
            BeanUtils.copyProperties(tagSearch,query);
        }
        // 设置分页属性和排序类型
        Integer pageIndex = (queryDto.getPageIndex()-1)*queryDto.getPageSize();
        // 设置开始位置
        query.setPageIndex(pageIndex);
        // 设置结束位置
        query.setPageSize(queryDto.getPageSize());
        // 设置mode
        query.setSortMode(queryDto.getSortMode());
        // 分割标签
        if(queryDto.getTags().size()>0){
            JSONArray tags = queryDto.getTags();
            String tag = "";
            for(int i=0;i<tags.size();i++){
                log.info(tags.get(i).toString());
                tag+=tags.get(i);
            }
            query.setTags(tag);
        }
        // 查询
        List<SearchVo> searchVoList = reportService.queryByDetails(query);
        // 转换时间
        for(int i=0;i<searchVoList.size();i++){
            // 获得时间
            SearchVo searchVo = searchVoList.get(i);
            if(searchVo.getNextTime()!=null){
                // 使用 LocalDate 将日期加一天
                LocalDate localDate = searchVo.getNextTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                localDate = localDate.plusDays(1);
                Date newDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                searchVo.setNextTime(newDate);
            }
            //
            if(searchVo.getDiagnoseTime()!=null){
                // 使用 LocalDate 将日期加一天
                LocalDate localDate = searchVo.getDiagnoseTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                localDate = localDate.plusDays(1);
                Date newDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                searchVo.setDiagnoseTime(newDate);
            }
            if(searchVo.getCheckTime()!=null){
                // 使用 LocalDate 将日期加一天
                LocalDate localDate = searchVo.getCheckTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                localDate = localDate.plusDays(1);
                Date newDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                searchVo.setCheckTime(newDate);
            }
            List<PulmonaryNoduleVo> noduleList = searchVo.getNoduleList();
            for(int j=0;j<noduleList.size();j++){
                PulmonaryNoduleVo pulmonaryNoduleVo = noduleList.get(j);
                // 获取到图片列表
                if(pulmonaryNoduleVo.getImage()!=null){
                    String[] images= pulmonaryNoduleVo.getImage().split(";");
                    pulmonaryNoduleVo.setImageList(Arrays.asList(images));
                }
            }
            // 设置noduleList
            searchVo.setNoduleList(noduleList);
        }
        return Result.ok(searchVoList);
    }

    @PostMapping("/update")
    public Result update(@RequestBody ReportDto reportDto) {
        Report report = new Report();
        BeanUtils.copyProperties(reportDto, report);
        JSONArray tagList = reportDto.getTagList();
        StringBuilder tags = new StringBuilder();
        for (int i = 0; i < tagList.size(); i++) {
            try {
                // 获取当前索引处的值
                Object value = tagList.get(i);
                // 这里你可以根据具体情况处理 value，比如转换成字符串
                String tagValue = value.toString();
                // 拼接
                tags.append(tagValue).append(",");
            } catch (JSONException e) {
                // 处理异常，比如 JSON 解析错误
                e.printStackTrace();
            }
        }
        report.setTagSet(tags.toString());
        JSONArray nodules = reportDto.getNoduleList();
        for (int i = 0; i < nodules.size(); i++) {
            Object value = nodules.get(i);
            Gson gson = new Gson();
            PulmonaryNodule pulmonaryNodule = gson.fromJson(value.toString(), PulmonaryNodule.class);
            log.info(pulmonaryNodule.toString());
            UpdateWrapper<PulmonaryNodule> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("nodule_id",pulmonaryNodule.getNoduleId());
            int insert = pulmonaryNoduleService.getBaseMapper().update(pulmonaryNodule,updateWrapper);
            if (insert <= 0) {
                return Result.fail();
            }
        }
        log.info(report.toString());
        boolean flag = reportService.updateReport(report);
        if (!flag) return Result.error("更新失败");
        return Result.ok("更新成功");
    }

    /**
     * @param historyDto
     * @return 获取历史随访记录
     */
    @PostMapping("/getReportByPatientId")
    public Result queryVO(@RequestBody HistoryDto historyDto) {
        String id = historyDto.getPatientId();
        // 分页
        historyDto.setPageIndex((historyDto.getPageIndex() - 1) * historyDto.getPageSize());
        List<Report> reports = reportService.queryByTimeAndId(historyDto);
        List<ReportVO2> result = new ArrayList<>();
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        // 查询病人
        queryWrapper.eq("admission_number", id);
        int suffix = historyDto.getPageIndex();
        Patient patient = patientService.getBaseMapper().selectOne(queryWrapper);
        for (int i = 0; i < reports.size(); i++) {
            ReportVO2 reportVo3 = new ReportVO2();
            // 拷贝属性
            BeanUtils.copyProperties(reports.get(i), reportVo3);
            // 设置次数
            reportVo3.setSeconds(suffix + i + 1);
            reportVo3.setReportId(reports.get(i).getReportId());
            Date nextTime = reports.get(i).getNextTime();
            // 将 Date 转换为 LocalDate
            if(nextTime!=null){
                LocalDate localNextTime = nextTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                // 添加一天
                localNextTime = localNextTime.plusDays(1);
                // 将 LocalDate 转换回 Date
                nextTime = java.sql.Date.valueOf(localNextTime);
                reportVo3.setNextTime(nextTime);
            }
            Date diagnoseTime = reports.get(i).getDiagnoseTime();
            // 将 Date 转换为 LocalDate
            if(diagnoseTime!=null){
                LocalDate localNextTime = diagnoseTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                // 添加一天
                localNextTime = localNextTime.plusDays(1);
                // 将 LocalDate 转换回 Date
                diagnoseTime = java.sql.Date.valueOf(localNextTime);
                reportVo3.setDiagnoseTime(diagnoseTime);
            }
            result.add(reportVo3);
        }
        return Result.ok(result);
    }

    /**
     * 分页查询
     * @return 返回分页结果
     */
    @PostMapping("/getLangCancerByPatientId")
    public Result getLangCaner(@RequestBody PatientPageDto page){
        Integer pageIndex = page.getPageIndex();
        page.setPageIndex((pageIndex - 1) * page.getPageSize());
        List<Report> reports = reportService.getLimit(page);
        int suffix = page.getPageIndex();
        List<ReportVo3> result = new ArrayList<>();
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        // 查询病人
        queryWrapper.eq("admission_number", page.getPatientId());
        Patient patient = patientService.getBaseMapper().selectOne(queryWrapper);
        for (int i = 0; i < reports.size(); i++) {
            ReportVo3 reportVo3 = new ReportVo3();
            // 拷贝属性
            BeanUtils.copyProperties(reports.get(i), reportVo3);
            // 设置次数
            reportVo3.setSeconds(suffix + i + 1);
            reportVo3.setReportId(reports.get(i).getReportId());
            Date nextTime = reports.get(i).getNextTime();
            Date diagnoseTime = reports.get(i).getDiagnoseTime();
            // 将 Date 转换为 LocalDate
            if(nextTime!=null){
                LocalDate localNextTime = nextTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                // 添加一天
                localNextTime = localNextTime.plusDays(1);
                // 将 LocalDate 转换回 Date
                nextTime = java.sql.Date.valueOf(localNextTime);
                reportVo3.setCreateTime(nextTime);
                reportVo3.setDoctorId(patient.getDoctorId());
            }
            if (diagnoseTime != null) {
                LocalDate localNextTime = diagnoseTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                // 添加一天
                localNextTime = localNextTime.plusDays(1);
                // 将 LocalDate 转换回 Date
                diagnoseTime = java.sql.Date.valueOf(localNextTime);
                reportVo3.setCreateTime(diagnoseTime);
                reportVo3.setDoctorId(patient.getDoctorId());
            }
            result.add(reportVo3);
        }
        return Result.ok(result);
    }

    /**
     * 删除记录
     * @param id reportId
     * @return 是否删除功能
     */
    @GetMapping("/delete/{id}")
    @Transactional
    public Result deleteReport(@PathVariable("id")String id){
        // 是否成功
        boolean flag = pulmonaryNoduleService.deleteNodulesById(id);
        // 删除report记录
        flag = reportService.getBaseMapper().deleteById(id)>0;
        if(!flag)return Result.fail();
        return Result.ok("删除成功");
    }
    /**
     * 添加肺癌信息或者更新肺癌信息
     * @param report 病人信息
     * @return 是否更新成功
     */
    @PostMapping("/langCancer")
    public Result insertCanerInfo(@RequestBody Report report){
        log.info("success");
        log.info(report.toString());
        UUID uuid = UUID.randomUUID();
        while(reportService.checkExist(uuid.toString())>0){
            uuid = UUID.randomUUID();
        }
        report.setReportId(uuid.toString());
        // 设置类型
        report.setPatientType("肺癌");
        int success = reportService.getBaseMapper().insert(report);
        return success>0? Result.ok("添加成功"):Result.fail();
    }

    /**
     * 删除记录
     * @param id 删除记录
     * @return 删除记录
     */
    @GetMapping("/deleteLangCancer/{id}")
    public Result deleteCancer(@PathVariable String id){
        int success = reportService.getBaseMapper().deleteById(id);
        return success>0? Result.ok("删除成功"):Result.fail();
    }

    /**
     * 修改肺癌信息
     * @param report 接收属性
     * @return 是否修改成功
     */
    @PostMapping("/updateCancer")
    @Transactional
    public Result updateCancerInfo(@RequestBody ReportDto report){
        // 获取到病人的身份证号码
        Report rep = new Report();
        BeanUtils.copyProperties(report,rep);
        boolean flag = reportService.updateReport(rep);
        return flag? Result.ok("修改成功"):Result.fail();
    }

    /**
     * 获取单个肺癌报告
     * @param id 报告id
     * @return 报告
     */
    @GetMapping("/getCancerInfoById/{id}")
    public Result getCancerById(@PathVariable("id")String id){
        QueryWrapper<Report>query = new QueryWrapper<>();
        query.eq("report_id",id);
        Report report = reportService.getBaseMapper().selectOne(query);
        return Result.ok(report);

    }


}
