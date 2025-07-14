package com.xju.hospital.controller;

import com.xju.hospital.entity.Tag;
import com.xju.hospital.service.TagService;
import com.xju.hospital.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@Slf4j
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 获取所有标签
     * @return
     */
    @RequestMapping("/getTags")
    public Result getTags(){
        List<Tag> all = tagService. getAll();
        return all.size()>0?Result.ok(all):Result.error();
    }


}
