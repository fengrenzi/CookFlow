package com.hnit.system.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class AddSuggestionDTO {
    @NotBlank
    private String title;
    private String content;
}