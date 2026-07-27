package com.hnit.system.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ForumActivityDto {
    @NotBlank(message = "标题不能为空")
    private String title;

    private String summary;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    private String category;
    private String tag;

    private List<String> imageIds;     // Banner图片ID列表（通常取第一个作为主图）
}