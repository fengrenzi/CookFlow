package com.hnit.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分类查询参数")
public class CategoryQueryDto {

    @ApiModelProperty(value = "分类ID（精确查询）")
    private String id;

    @ApiModelProperty(value = "关联表名（如 ingredients、recipes）")
    private String tableName;

    @ApiModelProperty(value = "父分类ID（查询子分类时传入，不传或传空字符串查询顶级分类）")
    private String parentId;

    @ApiModelProperty(value = "分类名称（模糊查询）")
    private String name;

    @ApiModelProperty(value = "页码（可选，用于分页）")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小（可选，用于分页）")
    private Integer pageSize = 10;
}