package com.xju.hospital.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xju.hospital.Vo.ReportVO;
import com.xju.hospital.entity.*;
import com.xju.hospital.Vo.PatientVO;
import com.xju.hospital.service.*;
import com.xju.hospital.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private PulmonaryNoduleService pulmonaryNoduleService;

    /**
     * 分页查询医生信息
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 医生列表
     */
    @GetMapping("/getDoctorList/{pageNumber}/{pageSize}")
    public Result getDoctorList(@PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize) {
        if (pageNumber == null || pageSize == null)
            return Result.error("页码为空");
        int offset = (pageNumber - 1) * pageSize;
        return Result.ok(doctorService.getAll(offset, pageSize));
    }

    /**
     * 分页查询患者信息
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 患者列表
     */
    @GetMapping("/getPatientList/{pageNumber}/{pageSize}")
    public Result getPatientList(@PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize) {
        if (pageNumber == null || pageSize == null)
            return Result.error("页码为空");
        int offset = (pageNumber - 1) * pageSize;
        List<Patient> patients = patientService.getPatients(offset, pageSize);
        return Result.ok(patients);
    }

    /**
     * 分页标签信息
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 标签列表
     */
    @GetMapping("/getTagsList/{pageNumber}/{pageSize}")
    public Result getTagsList(@PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize) {
        if (pageNumber == null || pageSize == null)
            return Result.error("页码为空");
        int offset = (pageNumber - 1) * pageSize;
        return Result.ok(tagService.getLimit(offset, pageSize));
    }

    /**
     * 分页标签信息
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 标签列表
     */
    @GetMapping("/getApplyList/{pageNumber}/{pageSize}")
    public Result getApplyList(@PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize) {
        if (pageNumber == null || pageSize == null)
            return Result.error("页码为空");
        int offset = (pageNumber - 1) * pageSize;
        return Result.ok(applyService.getLimit(offset, pageSize));
    }

    /**
     * 同意申请
     * 
     * @param id 申请的号
     * @return 是否申请成功
     */

    @GetMapping("/agreeApply/{id}")
    @Transactional
    public Result agreeApply(@PathVariable("id") String id) {
        QueryWrapper<Apply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("apply_id", id);
        Apply apply = applyService.getBaseMapper().selectOne(queryWrapper);
        Doctor doctor = new Doctor();
        doctor.setDoctorId(apply.getApplyId());
        doctor.setPassword(apply.getPassword());
        int success = doctorService.getBaseMapper().insert(doctor);
        if (success < 0)
            return Result.fail();
        boolean flag = applyService.deleteById(id);
        if (!flag)
            return Result.fail();
        return Result.ok("删除成功");
    }

    /**
     * 不同意申请
     * 
     * @param id 申请的号
     * @return 是否申请成功
     */
    @GetMapping("/disAgreeApply/{id}")
    public Result disagreeApply(@PathVariable("id") String id) {
        boolean flag = applyService.deleteById(id);
        if (!flag)
            return Result.fail();
        return Result.ok("删除成功");
    }

    /**
     * 删除患者
     * 
     * @param id 删除患者
     * @return 是否删除成功
     */
    @GetMapping("/removePatient/{id}")
    @Transactional
    public Result removePatient(@PathVariable("id") String id) {
        // 获取患者
        Patient patient = patientService.getBaseMapper().selectById(id);
        // 获取身份证
        String idCard = patient.getAdmissionNumber();
        // 查询所有的report
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_id", idCard);
        // 获取到所有的报告
        List<Report> reports = reportService.getBaseMapper().selectList(queryWrapper);
        // 根据报告获取到Nodule,然后删除
        for (int i = 0; i < reports.size(); i++) {
            String reportId = reports.get(i).getReportId();
            QueryWrapper<PulmonaryNodule> wrapper = new QueryWrapper<>();
            wrapper.eq("report_id", reportId);
            pulmonaryNoduleService.getBaseMapper().delete(wrapper);
        }
        // 删除病人所有相关的报告
        queryWrapper.clear();
        queryWrapper.eq("patient_id", idCard);
        reportService.getBaseMapper().delete(queryWrapper);
        // 最后删除病人
        boolean flag = patientService.removeById(id);
        if (!flag)
            return Result.fail();
        return Result.ok("删除成功");
    }

    /**
     * 删除医生
     * 
     * @param id 医生id
     * @return 是否删除成功
     */
    @GetMapping("/removeDoctor/{id}")
    @Transactional
    public Result removeDoctor(@PathVariable("id") String id) {
        boolean flag = doctorService.deleteByDoctorId(id);
        if (!flag)
            return Result.fail();
        return Result.ok("删除成功");
    }

    /**
     * 禁用标签
     * 
     * @param id 标签号
     * @return 是否禁用成功
     */
    @GetMapping("/disabledTag/{id}")
    public Result disabledTags(@PathVariable("id") String id) {
        UpdateWrapper<Tag> queryWrapper = new UpdateWrapper<>();
        queryWrapper.set("is_disabled", 1);
        queryWrapper.eq("tag_id", id);
        int success = tagService.getBaseMapper().update(new Tag(), queryWrapper);
        if (success < 0)
            return Result.fail();
        return Result.ok("删除成功");
    }

    /**
     * 删除id
     * 
     * @param id 根据id删除标签
     * @return 是否删除成功
     */
    @GetMapping("/removeTag/{id}")
    public Result deleteTag(@PathVariable("id") String id) {
        int success = tagService.getBaseMapper().deleteById(id);
        if (success < 0)
            return Result.fail();
        return Result.ok("删除成功");
    }

    /**
     * 添加标签
     * 
     * @param tag 标签
     * @return 是否添加成功
     */
    @PostMapping("/addTag")
    public Result addTag(@RequestBody Tag tag) {
        UUID uuid = UUID.randomUUID();
        while (tagService.idExist(uuid.toString()) > 0) {
            uuid = UUID.randomUUID();
        }
        tag.setTagId(uuid.toString());
        return Result.ok(tagService.getBaseMapper().insert(tag));
    }
}
