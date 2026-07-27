package com.hnit.system.domain.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class IngredientSimpleVO {
    private String id;
    private String name;
    private BigDecimal amount;
    private String unit;
    private String imageUrl;
}