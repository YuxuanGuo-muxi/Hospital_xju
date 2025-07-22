package com.xju.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xju.hospital.entity.Patient;
import com.xju.hospital.Vo.PatientVO;
import com.xju.hospital.entity.Report;
import com.xju.hospital.service.PatientService;
import com.xju.hospital.service.TagService;
import com.xju.hospital.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/patient")
@Slf4j
public class PatientController {
    /**
     * 服务对象
     */
    @Autowired
    private PatientService patientService;
    @Autowired
    private TagService tagService;

    /**
     * 向patient表中添加一条数据
     * @param patient 患者信息
     * @return 是否成功
     */
    @PostMapping("/add")
    public Result insert(@RequestBody Patient patient) {
        int insert = patientService.getBaseMapper().insert(patient);
        return insert == 1 ? Result.ok("添加成功") : Result.error("添加失败");
    }

    /**
     * 多表
     * @return
     */
    @GetMapping("/get/{doctorId}")
    public Result getByDoctorId(@PathVariable("doctorId")String doctorId){
        List<PatientVO> infos = patientService.getInfos(doctorId);
        // 结果列表
        List<PatientVO> result = new ArrayList<>();
        HashMap<String,List<String>>hashMap = new HashMap<>();
        for(int i=0;i< infos.size();i++){
            String id = infos.get(i).getPatientId();
            List<String> list = tagService.getTags(id);
            if(hashMap.get(id)==null){
                hashMap.put(id,list);
                PatientVO patientVO = new PatientVO();
                BeanUtils.copyProperties(infos.get(i),patientVO);
                result.add(patientVO);
            }
        }
        int count = 0;
        for (Map.Entry<String,List<String>> entry : hashMap.entrySet()) {
            List<String> tags = entry.getValue();
            String s = "";
            for(int i=0;i< tags.size();i++){
                s+=tags.get(i)+",";
            }
            result.get(count).setTagName(s);
            count++;
        }
        if(infos.size()<0)return Result.fail();
        return Result.ok(result);
    }

    /**
     * 时间排序
     * @return 返回排序后的数据
     */
    @GetMapping("/getByTime/{order}/{id}")
    public Result getByTime(@PathVariable("order")Integer order,@PathVariable("id")String doctorId){
        List<PatientVO> infos = new ArrayList<>();
        if(order==1){
            infos = patientService.getAsc(doctorId);
        }else{
            infos = patientService.getDesc(doctorId);
        }
        
        // 结果列表
        List<PatientVO> result = new ArrayList<>();
        HashMap<String,List<String>>hashMap = new HashMap<>();
        for(int i=0;i< infos.size();i++){
            String id = infos.get(i).getPatientId();
            List<String> list = tagService.getTags(id);
            if(hashMap.get(id)==null){
                hashMap.put(id,list);
                PatientVO patientVO = new PatientVO();
                BeanUtils.copyProperties(infos.get(i),patientVO);
                result.add(patientVO);
            }
        }

        int count = 0;
        for (Map.Entry<String,List<String>> entry : hashMap.entrySet()) {
            List<String> tags = entry.getValue();
            String s = "";
            for(int i=0;i< tags.size();i++){
                s+=tags.get(i)+",";
            }
            result.get(count).setTagName(s);
            count++;
        }
        if(infos.size()<0)return Result.fail();
        return Result.ok(result);
    }

    /**
     * 获取到病人详细信息
     * @param id 病人id
     * @return 返回单个病人的信息
     */
    @GetMapping("/getById/{id}")
    public Result get(@PathVariable("id") String id){
        QueryWrapper<Patient>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("admission_number",id);
        Patient patient = patientService.getBaseMapper().selectOne(queryWrapper);
        return patient!=null?Result.ok(patient):Result.fail();
    }

    /**
     * 更新数据库信息
     * @param patient 医生信息
     * @return 是否成功
     */
    @PostMapping("/update")
    public Result updateInfo(@RequestBody Patient patient){
        return patientService.updateById(patient)?Result.ok("修改成功"):Result.fail();
    }

}

