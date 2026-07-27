package com.hnit.system.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ForumShareVo {
    private String id;
    private String title;
    private String content;
    private String type;
    private String resourceId;
    private String category;
    private String tags;
    private String difficulty;
    private Boolean isPublic;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private Long viewCount;
    private Integer status;
    private LocalDateTime createdAt;
    private Boolean liked;
    private Boolean favorited;
    private String imageUrl;        // 兼容旧字段
    private List<String> imageUrls; // 新增（方案一）
}