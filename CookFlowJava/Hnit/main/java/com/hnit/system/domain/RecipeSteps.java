package com.hnit.system.domain;

import com.hnit.common.annotation.Excel;

/**
 * 菜谱步骤（有序）对象 recipe_steps
 * 
 * @author Z
 * @date 2026-03-23
 */
public class RecipeSteps extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 步骤ID，自增 */
    private Long id;

    /** 所属菜谱ID */
    @Excel(name = "所属菜谱ID")
    private Long recipeId;

    /** 步骤顺序编号，从0或1开始 */
    @Excel(name = "步骤顺序编号，从0或1开始")
    private Long stepIndex;

    /** 步骤内容（文本或富文本） */
    @Excel(name = "步骤内容", readConverterExp = "文=本或富文本")
    private String content;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRecipeId(Long recipeId) 
    {
        this.recipeId = recipeId;
    }

    public Long getRecipeId() 
    {
        return recipeId;
    }

    public void setStepIndex(Long stepIndex) 
    {
        this.stepIndex = stepIndex;
    }

    public Long getStepIndex() 
    {
        return stepIndex;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("recipeId", getRecipeId())
            .append("stepIndex", getStepIndex())
            .append("content", getContent())
            .toString();
    }
}
