package com.xju.hospital.controller;

import com.xju.hospital.entity.Apply;
import com.xju.hospital.service.ApplyService;
import com.xju.hospital.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apply")
@Slf4j
public class ApplyController {
    @Autowired
    private ApplyService applyService;

    /**
     * 注册接口
     * @param apply 申请实体
     * @return 是否注册成功
     */
    @PostMapping("/register")
    public Result register(@RequestBody Apply apply) {
        int success = applyService.getBaseMapper().insert(apply);
        return success>0? Result.ok("申请成功"):Result.fail();
    }
}
