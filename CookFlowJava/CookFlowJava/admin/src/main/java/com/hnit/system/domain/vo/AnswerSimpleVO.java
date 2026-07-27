package com.hnit.system.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AnswerSimpleVO {
    private String id;
    private String questionTitle;
    private String content;
    private Integer likes;
    private Integer comments;
    private LocalDateTime date;
}