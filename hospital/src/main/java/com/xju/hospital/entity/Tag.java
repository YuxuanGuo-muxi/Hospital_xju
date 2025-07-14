package com.xju.hospital.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tags")
public class Tag {
    // 标签号
    @TableField("tag_id")
    private String tagId;
    // 标签名
    @TableField("tag_name")
    private String tagName;
    // 标签描述
    @TableField("tag_description")
    private String tagDescription;
    // 标签相关的数量
    @TableField("count")
    private Integer count;
    // 是否禁用(0禁用，1启用)
    @TableField("is_disabled")
    private Integer isDisabled;
}
