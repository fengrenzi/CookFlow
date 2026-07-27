package com.hnit.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("images_record")
public class ImagesRecord {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String tableName;
    private String recipeId;
    private String imageId;
    private Integer sort;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 非数据库字段，用于前端展示图片URL
    @TableField(exist = false)
    private String imageUrl;
}