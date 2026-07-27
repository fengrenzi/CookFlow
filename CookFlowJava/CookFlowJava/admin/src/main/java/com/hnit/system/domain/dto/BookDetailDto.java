package com.hnit.system.domain.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BookDetailDto {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private BigDecimal price;
    private String coverUrl;
    private String description;
    private Integer totalPages;
    private Integer recipeCount;
    private Integer difficulty;
    private String remark;
    private Integer readCount;
    private Integer recommendCount;
    private Integer readingCount;
    private Integer salesCount;
    private BigDecimal ratingScore;
    private Integer ratingPeople;
    private List<RecipesDto> recipes;
}