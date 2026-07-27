package com.hnit.system.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BookStats {
    private String bookId;
    private Integer readCount;
    private Integer recommendCount;
    private Integer readingCount;
    private Integer salesCount;
    private BigDecimal ratingScore;
    private Integer ratingPeople;
    private Date updatedAt;
}