package com.hnit.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("sensitive_word")
public class SensitiveWord {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String word;
    private String replaceWith;
    private String level;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}