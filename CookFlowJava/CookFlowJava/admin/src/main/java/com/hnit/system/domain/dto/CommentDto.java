package com.hnit.system.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class CommentDto {
    private String resourceType;
    private String resourceId;
    private String parentId;
    private String content;
    private Integer rating;
    private List<String> imageIds;
}