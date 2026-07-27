package com.hnit.system.domain;

import java.math.BigDecimal;
import com.hnit.common.annotation.Excel;

/**
 * 菜谱与食材关联（含数量）对象 recipe_ingredients
 * 
 * @author Z
 * @date 2026-03-23
 */
public class RecipeIngredients extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联菜谱ID */
    private Long recipeId;

    /** 关联食材ID */
    private String ingredientId;

    /** 数量（按照 unit 单位） */
    @Excel(name = "数量", readConverterExp = "按=照,u=nit,单=位")
    private BigDecimal amount;

    /** 计量单位（覆盖 ingredients.unit） */
    @Excel(name = "计量单位", readConverterExp = "覆=盖,i=ngredients.unit")
    private String unit;

    public void setRecipeId(Long recipeId) 
    {
        this.recipeId = recipeId;
    }

    public Long getRecipeId() 
    {
        return recipeId;
    }

    public void setIngredientId(String ingredientId) 
    {
        this.ingredientId = ingredientId;
    }

    public String getIngredientId() 
    {
        return ingredientId;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recipeId", getRecipeId())
            .append("ingredientId", getIngredientId())
            .append("amount", getAmount())
            .append("unit", getUnit())
            .toString();
    }
}
