package com.hnit.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)  // 忽略空值字段
@ApiModel(value = "菜谱查询参数")
public class RecipeQueryDto {
    @ApiModelProperty(value = "搜索关键词（菜谱名称/食材/作者）")
    private String keyword;

    @ApiModelProperty(value = "分类ID")
    private String categoryId;

    @ApiModelProperty(value = "排序方式（newest/popular/difficulty）")
    private String sort;

    @ApiModelProperty(value = "页码，默认1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页数量，默认10")
    private Integer pageSize = 10;

    @ApiModelProperty(hidden = true)
    private Integer offset;
}