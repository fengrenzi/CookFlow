package com.hnit.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConversationMessage {
    private Long id;
    private Long sessionId;
    private String role; // user, assistant
    private String content;
    private LocalDateTime timestamp;
}