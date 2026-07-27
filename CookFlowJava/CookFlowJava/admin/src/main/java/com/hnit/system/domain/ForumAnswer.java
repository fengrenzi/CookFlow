package com.hnit.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("forum_answers")
public class ForumAnswer {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String questionId;
    private String content;
    private Long userId;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isAccepted;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}