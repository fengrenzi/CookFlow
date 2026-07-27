package com.hnit.system.domain;

import lombok.Data;

import java.util.Date;

@Data
public class BookRecipe {
    private Long id;            // 自增主键
    private String bookId;      // 书籍ID
    private String recipeId;    // 菜谱ID
    private Integer pageNumber; // 页码
    private Integer sortOrder;  // 排序
    private Date createdAt;     // 创建时间
}