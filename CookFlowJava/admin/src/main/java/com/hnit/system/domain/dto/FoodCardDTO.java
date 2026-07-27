package com.hnit.system.domain.dto;

import lombok.Data;

@Data
public class FoodCardDTO {
    private String recipeId;    // UUID
    private String title;
    private String imageUrl;
    private Integer difficulty;
    private Integer prepTime;
    private String description;
    private Boolean isSpecialty;
}