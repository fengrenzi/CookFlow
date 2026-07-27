package com.hnit.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("forum_shares")
public class ForumShare {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String title;
    private String content;
    private String type;          // image / video
    private String resourceId;    // 图片ID 或 视频URL
    private String category;
    private String tags;          // JSON 字符串
    private String difficulty;
    private Boolean isPublic;
    private Long userId;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private Long viewCount;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}