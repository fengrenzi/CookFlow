package com.hnit.system.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class JoinActivityDTO {
    @NotBlank
    private String activityId;
}