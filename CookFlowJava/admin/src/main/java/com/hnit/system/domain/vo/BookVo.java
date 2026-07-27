package com.hnit.system.domain.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookVo {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private BigDecimal price;
    private String coverImageId;
    private String coverUrl;
    private String description;
    private Integer totalPages;
    private Integer recipeCount;
    private Integer status;
    private Integer difficulty;
    private Integer readCount;
    private Integer recommendCount;
    private Integer readingCount;
    private Integer salesCount;
    private BigDecimal ratingScore;
    private Integer ratingPeople;
}