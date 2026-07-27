package com.hnit.system.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ForumQuestionDto {
    @NotBlank(message = "标题不能为空")
    @Size(min = 5, max = 100, message = "标题长度5-100字")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(max = 2000, message = "内容不超过2000字")
    private String content;

    private List<String> imageIds;   // 图片ID列表（可选）
    private List<String> tags;       // 标签列表
}