package com.hnit.system.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class UpdateCartQuantityDTO {
    @NotNull
    private Integer quantity;
}