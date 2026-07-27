package com.hnit.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_interactions")
public class UserInteraction {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private Long userId;
    private String targetType;      // share/question/answer/activity
    private String targetId;
    private String interactionType; // like/favorite/follow/participate
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}