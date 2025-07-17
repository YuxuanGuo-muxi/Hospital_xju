package com.xju.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xju.hospital.entity.Apply;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ApplyMapper extends BaseMapper<Apply> {
    List<Apply> getLimit(@Param("offset") int offset, @Param("limit") int limit);

    int deleteById(@Param("id") String id);

    
}
