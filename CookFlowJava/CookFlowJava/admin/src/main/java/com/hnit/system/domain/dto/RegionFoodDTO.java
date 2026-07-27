package com.hnit.system.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RegionFoodDTO {
    private String code;
    private String name;
    private Integer level;
    private BigDecimal lng;
    private BigDecimal lat;
    private Integer foodCount;
    private List<FoodCardDTO> foods;
}

