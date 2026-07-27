package com.hnit.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "菜谱食材关联")
public class RecipeIngredients {
    @ApiModelProperty(value = "关联ID（UUID）")
    private String id;

    @ApiModelProperty(value = "菜谱ID")
    private String recipeId;

    @ApiModelProperty(value = "食材分类ID（关联 category.id）")
    private String categoryId;

    @ApiModelProperty(value = "数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "单位")
    private String unit;
}