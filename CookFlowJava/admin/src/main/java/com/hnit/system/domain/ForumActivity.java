package com.hnit.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("forum_activities")
public class ForumActivity {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String title;
    private String summary;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String category;
    private String tag;
    private Long userId;
    private Integer participantCount;
    private Integer status; // 0进行中 1已结束 2草稿
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}