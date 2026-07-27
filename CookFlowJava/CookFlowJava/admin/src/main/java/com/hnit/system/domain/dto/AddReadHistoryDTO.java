package com.hnit.system.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddReadHistoryDTO {
    @NotBlank
    private String bookId;
    @NotNull
    private Integer progress;
}