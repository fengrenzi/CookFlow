package com.hnit.system.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookDto {
    private String id;
    private String title;
    private String author;
    private BigDecimal price;
    private String coverUrl;
    private BigDecimal ratingScore;
    private Boolean isNew;
    private Boolean isHot;
    private Integer difficulty;
    private Integer readCount;
    private Integer salesCount;
}