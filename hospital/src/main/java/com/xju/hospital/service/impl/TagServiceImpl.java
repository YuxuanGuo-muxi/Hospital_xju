package com.xju.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xju.hospital.entity.Tag;
import com.xju.hospital.mapper.TagMapper;
import com.xju.hospital.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<String> getTags(String patientId) {
        return tagMapper.getTags(patientId);
    }

    /**
     * 获取所有tags标签
     * @return  tags列表
     */
    @Override
    public List<Tag> getAll() {
        return tagMapper.getAll();
    }

    /**
     * 分页查询
     * @param offset 分页查询
     * @param limit 分页查询标签
     * @return 分页后的结果
     */
    @Override
    public List<Tag> getLimit(int offset, int limit) {
        return tagMapper.getTagsLimit(offset,limit);
    }

    /**
     * 删除标签
     * @param patientId 病人id
     * @return 返回是否成功
     */
    @Override
    public boolean deleteTags(String patientId) {
        return tagMapper.deleteTags(patientId)>0;
    }

    /**
     * 判断id是否存在
     * @param tagId id是否存在
     * @return 是否存在
     */
    @Override
    public Integer idExist(String tagId) {
        return tagMapper.checkIdExists(tagId);
    }
}
