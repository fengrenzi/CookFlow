package com.hnit.system.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ForumAnswerDto {
    @NotBlank(message = "回答内容不能为空")
    @Size(max = 2000, message = "回答内容不超过2000字")
    private String content;
}