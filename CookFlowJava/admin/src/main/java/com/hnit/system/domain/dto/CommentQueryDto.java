package com.hnit.system.domain.dto;

import lombok.Data;

@Data
public class CommentQueryDto {
    private String resourceType;
    private String resourceId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String sort = "created_desc";
}