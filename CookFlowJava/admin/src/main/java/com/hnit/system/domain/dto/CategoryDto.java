package com.hnit.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "分类列表返回数据")
public class CategoryDto {

    @ApiModelProperty(value = "分类ID")
    private String id;

    @ApiModelProperty(value = "父分类ID")
    private String parentId;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "完整图片URL")
    private String imageUrl;

    @ApiModelProperty(value = "检索次数")
    private Integer searchCount;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}