package com.hnit.system.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddToCartDTO {
    @NotBlank
    private String itemType;   // recipe, book, book_recipe
    @NotBlank
    private String itemId;
    @NotNull
    private Integer quantity;
}