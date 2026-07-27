package com.hnit.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)  // 忽略空值字段
public class RecipesDto {
    @ApiModelProperty(value = "菜谱ID（UUID）")
    private String id;

    @ApiModelProperty(value = "菜谱标题")
    private String title;

    @ApiModelProperty(value = "主图片")
    private String imgUrl;

    private String imgUrlsStr;

    @ApiModelProperty(value = "图片组")
    private List<String> imgUrls;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "作者用户名")
    private String authorName;

    @ApiModelProperty(value = "作者头像URL")
    private String authorAvatar;

    @ApiModelProperty(value = "分类名")
    private List<String> categoryNames;

    // 临时字段，用于接收 SQL 返回的逗号分隔分类名
    private String categoryNamesStr;

    @ApiModelProperty(value = "主要食材（名称列表）")
    private List<String> ingredients;

    // 临时字段用于接收 SQL 返回的逗号分隔食材名
    private String ingredientsStr;

}