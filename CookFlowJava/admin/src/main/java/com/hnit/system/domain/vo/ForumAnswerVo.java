package com.hnit.system.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumAnswerVo {
    private String id;
    private String questionId;
    private String content;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isAccepted;
    private LocalDateTime createdAt;
    private Boolean liked;
}