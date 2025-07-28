package com.xju.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xju.hospital.entity.Doctor;
import com.xju.hospital.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xju.hospital.util.Result;

@RestController
@RequestMapping("/api/doctor")
@Slf4j
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    /**
     * 登录功能
     * @return 登录是否成功
     */
    @PostMapping("/login")
    public Result login(@RequestBody Doctor doctor) {
        log.info(doctor.toString());
        String doctorId = doctor.getDoctorId();
        String password = doctor.getPassword();
        QueryWrapper<Doctor> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("doctor_id",doctorId);
        queryWrapper.eq("password",password);
        Doctor doctor1 = doctorService.getBaseMapper().selectOne(queryWrapper);
        if (doctor1==null){
            return Result.fail();
        }
        return Result.ok("登陆成功");
    }

    /**
     *鉴权
     * @param id 权限鉴别
     * @return 是否成功
     */
    @GetMapping("/getPower/{id}")
    public Result getPower(@PathVariable("id")String id){
        QueryWrapper<Doctor> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("doctor_id",id);
        Doctor doctor = doctorService.getBaseMapper().selectOne(queryWrapper);
        return Result.ok(doctor);
    }
}

