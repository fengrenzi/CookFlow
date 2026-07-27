package com.hnit.system.domain.dto;

import lombok.Data;

@Data
public class DialogueRequest {
    private String sessionId; // 前端维护的会话标识，可以是 UUID
    private Long userId;
    private String text;
}