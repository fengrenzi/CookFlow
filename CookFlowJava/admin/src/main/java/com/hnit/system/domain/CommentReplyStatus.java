package com.hnit.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentReplyStatus {
    private String id;
    private String commentId;
    private Long targetUserId;
    private Integer isRead;      // 0未读 1已读
    private LocalDateTime readTime;
    private LocalDateTime createTime;
}