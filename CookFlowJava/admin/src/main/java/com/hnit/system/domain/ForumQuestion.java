package com.hnit.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("forum_questions")
public class ForumQuestion {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String title;
    private String content;
    private Long userId;
    private Integer answerCount;
    private Integer favoriteCount;
    private Integer followCount;
    private Long viewCount;
    private Boolean isResolved;
    private Integer status; // 0正常 1删除 2关闭
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}