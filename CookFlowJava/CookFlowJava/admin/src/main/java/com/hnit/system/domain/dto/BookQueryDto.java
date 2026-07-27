package com.hnit.system.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookQueryDto {
    private String title;
    private String author;
    private Integer difficulty;       // 难度筛选
    private BigDecimal minRating;     // 最低评分
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy;            // 排序方式: price_asc, price_desc, newest, recommended, reading_count_desc, rating_desc
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer offset;           // 内部使用，用于分页
}