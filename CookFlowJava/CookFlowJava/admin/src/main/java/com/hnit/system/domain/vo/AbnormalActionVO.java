package com.hnit.system.domain.vo;

import lombok.Data;

@Data
public class AbnormalActionVO {
    private Long id;
    private String userName;
    private String action;
    private String riskLevel;
    private String time;
}