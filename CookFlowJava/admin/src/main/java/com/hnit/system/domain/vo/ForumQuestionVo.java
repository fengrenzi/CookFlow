package com.hnit.system.domain.vo;

import com.hnit.system.domain.ImagesRecord;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ForumQuestionVo {
    private String id;
    private String title;
    private String content;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Integer answerCount;
    private Integer favoriteCount;
    private Integer followCount;
    private Long viewCount;
    private Boolean isResolved;
    private LocalDateTime createdAt;
    private List<ImagesRecord> images;
    private List<String> tags;
    private Boolean favorited;
    private Boolean followed;
}