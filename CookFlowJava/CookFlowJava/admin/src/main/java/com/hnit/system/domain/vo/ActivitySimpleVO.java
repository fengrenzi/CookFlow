package com.hnit.system.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ActivitySimpleVO {
    private String id;
    private String title;
    private LocalDateTime date;
    private String time;
    private String location;
    private Integer participants;
    private String status;
    private String organizer;
    private String suggestion;
}