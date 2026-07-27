package com.hnit.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecipeStep {
    private Long id;
    private Long recipeId;
    private Integer stepNumber;
    private String description;
    private Long imageId;
    private Integer durationMinutes;
    private String tips;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}