package com.xju.hospital.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("apply")
public class Apply {
    private String applyId;
    private String password;
    private String comment;
}
