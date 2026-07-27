package com.hnit.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserBookReadHistory {
    private String id;
    private Long userId;
    private String bookId;
    private LocalDateTime lastReadTime;
    private Integer progress;    // 0-100
}