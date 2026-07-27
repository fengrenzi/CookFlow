package com.hnit.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "菜谱")
public class Recipes {
    @ApiModelProperty(value = "菜谱ID（UUID）")
    private String id;

    @ApiModelProperty(value = "菜谱标题")
    private String title;

    @ApiModelProperty(value = "主图图片ID")
    private String imageId;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "准备时间（分钟）")
    private Integer prepTime;

    @ApiModelProperty(value = "烹饪时间（分钟）")
    private Integer cookTime;

    @ApiModelProperty(value = "难度（简单/中等/困难）")
    private String difficulty;

    @ApiModelProperty(value = "收藏数")
    private Integer favoriteCount;

    @ApiModelProperty(value = "评论数")
    private Integer commentCount;

    @ApiModelProperty(value = "浏览量")
    private Long views;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @ApiModelProperty(value = "综合热度分")
    private Double score;

    @ApiModelProperty(value = "创建者ID")
    private Long createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "备注")
    private String remark;
}