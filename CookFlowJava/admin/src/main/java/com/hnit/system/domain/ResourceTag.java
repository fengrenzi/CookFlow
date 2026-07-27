package com.hnit.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resource_tags")
public class ResourceTag {
    private String resourceType;   // share/question
    private String resourceId;
    private String tagId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}