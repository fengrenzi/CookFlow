package com.hnit.system.domain;

import com.hnit.common.annotation.Excel;

/**
 * 食材详情静态内容对象 ingredient_details
 * 
 * @author hnit
 * @date 2026-03-23
 */
public class IngredientDetails extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 食材ID */
    private String ingredientId;

    /** 轮播图数据（JSON数组） */
    @Excel(name = "轮播图数据", readConverterExp = "J=SON数组")
    private String carousel;

    /** 挑选提示（JSON数组） */
    @Excel(name = "挑选提示", readConverterExp = "J=SON数组")
    private String selectionTips;

    /** 处理步骤（JSON数组） */
    @Excel(name = "处理步骤", readConverterExp = "J=SON数组")
    private String processingSteps;

    /** 营养信息（JSON数组） */
    @Excel(name = "营养信息", readConverterExp = "J=SON数组")
    private String nutrition;

    /** 科普知识点（JSON数组） */
    @Excel(name = "科普知识点", readConverterExp = "J=SON数组")
    private String knowledgePoints;

    public void setIngredientId(String ingredientId) 
    {
        this.ingredientId = ingredientId;
    }

    public String getIngredientId() 
    {
        return ingredientId;
    }

    public void setCarousel(String carousel) 
    {
        this.carousel = carousel;
    }

    public String getCarousel() 
    {
        return carousel;
    }

    public void setSelectionTips(String selectionTips) 
    {
        this.selectionTips = selectionTips;
    }

    public String getSelectionTips() 
    {
        return selectionTips;
    }

    public void setProcessingSteps(String processingSteps) 
    {
        this.processingSteps = processingSteps;
    }

    public String getProcessingSteps() 
    {
        return processingSteps;
    }

    public void setNutrition(String nutrition) 
    {
        this.nutrition = nutrition;
    }

    public String getNutrition() 
    {
        return nutrition;
    }

    public void setKnowledgePoints(String knowledgePoints) 
    {
        this.knowledgePoints = knowledgePoints;
    }

    public String getKnowledgePoints() 
    {
        return knowledgePoints;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ingredientId", getIngredientId())
            .append("carousel", getCarousel())
            .append("selectionTips", getSelectionTips())
            .append("processingSteps", getProcessingSteps())
            .append("nutrition", getNutrition())
            .append("knowledgePoints", getKnowledgePoints())
            .toString();
    }
}
