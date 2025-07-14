package com.xju.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xju.hospital.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {
    List<String>getTags(String patientId);
    List<Tag>getAll();
    // 标签分页查询
    List<Tag>getLimit(int offset,int limit);
    // 删除病人相关的标签信息
    boolean deleteTags(String patientId);
    // id是否存在
    Integer idExist(String tagId);
}
