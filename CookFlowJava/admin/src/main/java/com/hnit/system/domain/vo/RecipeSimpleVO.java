package com.hnit.system.domain.vo;

import lombok.Data;

@Data
public class RecipeSimpleVO {
    private String id;
    private String title;
    private String image;      // 封面图片URL
    private String author;
    private Integer likes;
    private Integer comments;
}