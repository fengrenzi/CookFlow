package com.hnit.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "分类树结构（支持多级）")
public class CategoryTreeDto {

    @ApiModelProperty(value = "分类ID")
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "完整图片URL（可能为null）")
    private String imageUrl;

    @ApiModelProperty(value = "子分类列表")
    private List<CategoryTreeDto> children;
}