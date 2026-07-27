package com.hnit.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "菜谱与分类关联")
public class RecipeCategory {
    @ApiModelProperty(value = "关联ID（UUID）")
    private String id;

    @ApiModelProperty(value = "菜谱ID")
    private String recipeId;

    @ApiModelProperty(value = "分类ID（关联 category.id）")
    private String categoryId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}