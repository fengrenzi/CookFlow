package com.hnit.system.domain;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserProfile {
    private String id;
    private Long userId;
    private String bio;
    private String realName;
    private Integer gender;      // 0未知 1男 2女
    private LocalDate birthday;
    private String location;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}