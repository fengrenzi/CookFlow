package com.hnit.system.domain.vo;

import lombok.Data;
import java.util.List;

@Data
public class ShoppingCartItemVO {
    private String cartId;
    private String itemType;
    private String itemId;
    private Integer quantity;
    private Object detail;          // 实际为 RecipeDetailVO / BookDetailVO / BookRecipeDetailVO
    private List<IngredientVO> ingredients;
}