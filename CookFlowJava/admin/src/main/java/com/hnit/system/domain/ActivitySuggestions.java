package com.hnit.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivitySuggestions {
    private String id;
    private Long userId;
    private String title;
    private String content;
    private Integer status;      // 0待审核 1已采纳 2已拒绝
    private LocalDateTime createTime;
}