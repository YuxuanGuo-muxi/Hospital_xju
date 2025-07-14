package com.xju.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xju.hospital.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    List<String> getTags(@Param("patientId")String patientId);
    // 获取所有标签
    List<Tag> getAll();
     // 获取标签名
    List<String>getTagNames();
    // 分页查询
    List<Tag> getTagsLimit(@Param("offset")Integer offset,@Param("limit")Integer limit);
    // 删除标签
    Integer deleteTags(@Param("patientId")String patientId);
    int checkIdExists(@Param("tagId")String tagId);

    int add(Tag tag);
}
