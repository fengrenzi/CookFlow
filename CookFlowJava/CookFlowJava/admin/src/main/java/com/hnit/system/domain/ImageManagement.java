package com.hnit.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel(value = "图片管理")
public class ImageManagement {
    @ApiModelProperty(value = "图片ID")
    private String id;

    @ApiModelProperty(value = "原图片名")
    private String originalName;

    @ApiModelProperty(value = "存储图片名")
    private String storedName;

    @ApiModelProperty(value = "存储位置")
    private String storagePath;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;
}