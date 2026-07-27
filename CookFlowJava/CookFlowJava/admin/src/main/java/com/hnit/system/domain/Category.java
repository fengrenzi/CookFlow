package com.hnit.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "通用分类")
public class Category {
    @ApiModelProperty(value = "分类ID（UUID）")
    private String id;

    @ApiModelProperty(value = "关联表名（recipes、ingredients）")
    private String tableName;

    @ApiModelProperty(value = "父分类ID")
    private String parentId;

    @ApiModelProperty(value = "分类名称")
    private String designation;

    @ApiModelProperty(value = "代表图片ID（关联 image_management.id）")
    private String imageId;

    @ApiModelProperty(value = "检索次数")
    private Integer searchCount;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}