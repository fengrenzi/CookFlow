package com.hnit.system.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReadHistoryVO {
    private String id;
    private String bookId;
    private String bookTitle;
    private String author;
    private String coverUrl;
    private LocalDateTime lastReadTime;
    private Integer progress;
}