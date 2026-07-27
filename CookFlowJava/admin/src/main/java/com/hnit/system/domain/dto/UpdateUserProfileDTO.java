package com.hnit.system.domain.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateUserProfileDTO {
    private String bio;
    private String realName;
    private Integer gender;
    private LocalDate birthday;
    private String location;
}