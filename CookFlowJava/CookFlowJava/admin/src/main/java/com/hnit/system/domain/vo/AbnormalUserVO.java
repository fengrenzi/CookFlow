package com.hnit.system.domain.vo;

import lombok.Data;

@Data
public class AbnormalUserVO {
    private Long userId;
    private String userName;
    private String type;
    private Integer score;
    private String time;
    private String status;
}