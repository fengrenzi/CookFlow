package com.hnit.system.domain.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserProfileVO {
    private Long userId;
    private String userName;
    private String email;
    private String phonenumber;
    private String avatar;
    private String bio;
    private String realName;
    private Integer gender;
    private LocalDate birthday;
    private String location;
    private LocalDateTime createTime;
}