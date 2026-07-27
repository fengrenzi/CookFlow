package com.hnit.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityParticipants {
    private String id;
    private String activityId;
    private Long userId;
    private Integer status;      // 0已取消 1已报名
    private LocalDateTime createTime;
}