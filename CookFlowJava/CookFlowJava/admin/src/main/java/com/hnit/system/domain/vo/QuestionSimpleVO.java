package com.hnit.system.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class QuestionSimpleVO {
    private String id;
    private String title;
    private String content;
    private Integer answers;
    private Integer views;
    private LocalDateTime date;
    private String author;
}