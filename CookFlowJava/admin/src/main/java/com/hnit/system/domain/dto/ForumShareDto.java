package com.hnit.system.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ForumShareDto {
    @NotBlank(message = "标题不能为空")
    @Size(min = 10, max = 50, message = "标题长度10-50字")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(min = 50, max = 1000, message = "内容长度50-1000字")
    private String content;

    private String type;               // image/video
    private String videoUrl;           // 视频URL（type=video时使用）
    private String category;
    private List<String> tags;
    private String difficulty;
    private Boolean isPublic = true;

    private List<String> imageIds;     // 图片ID列表（type=image时使用，最多9张）
}