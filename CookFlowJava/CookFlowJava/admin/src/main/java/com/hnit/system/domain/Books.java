package com.hnit.system.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Books {
    private String id;              // UUID
    private String title;           // 书名
    private String author;          // 作者
    private String publisher;       // 出版社
    private String isbn;            // ISBN
    private BigDecimal price;       // 价格
    private String coverImageId;    // 封面图片ID
    private String description;     // 简介
    private Integer totalPages;     // 总页数
    private Integer recipeCount;    // 菜谱数
    private Integer difficulty;     // 难度 1简单 2中等 3困难
    private Integer status;         // 状态 0上架 1下架 2待审核
    private Long createdBy;         // 创建者ID
    private Date createdAt;         // 创建时间
    private Date updatedAt;         // 更新时间
    private String remark;          // 备注
}