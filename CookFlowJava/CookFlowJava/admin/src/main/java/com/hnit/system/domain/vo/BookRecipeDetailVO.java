package com.hnit.system.domain.vo;

import lombok.Data;

@Data
public class BookRecipeDetailVO {
    private String bookTitle;
    private Integer pageNumber;
    private String recipeName;
    private String recipeImageUrl;
}