package com.hnit.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConversationSession {
    private Long id;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime lastActive;
    private Long currentRecipeId;
    private Integer currentStepIndex; // 当前步骤索引（从0开始）
    private Boolean cookingMode; // true 表示烹饪模式
    private String status; // active, closed
}