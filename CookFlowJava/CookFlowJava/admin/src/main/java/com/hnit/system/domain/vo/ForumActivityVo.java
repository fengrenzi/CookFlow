package com.hnit.system.domain.vo;

import com.hnit.system.domain.ImagesRecord;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ForumActivityVo {
    private String id;
    private String title;
    private String summary;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String category;
    private String tag;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Integer participantCount;
    private Integer status;
    private LocalDateTime createdAt;
    private Boolean participated;
    private List<ImagesRecord> images;   // 保留原有对象列表（兼容旧前端）
    private List<String> imageUrls;      // 新增：图片URL字符串列表，前端直接使用
}