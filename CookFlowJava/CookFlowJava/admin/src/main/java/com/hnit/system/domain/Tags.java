package com.hnit.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tags")
public class Tags {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String name;
    private String type;       // share/question
    private Integer useCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}