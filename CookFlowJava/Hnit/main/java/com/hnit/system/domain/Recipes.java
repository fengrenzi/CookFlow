package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * 菜谱对象 recipes
 * 
 * @author Z
 * @date 2026-03-23
 */
public class Recipes extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 菜谱ID，自增主键 */
    private Long id;

    /** 菜谱名称 */
    @Excel(name = "菜谱名称")
    private String name;

    /** 菜谱描述/正文 */
    @Excel(name = "菜谱描述/正文")
    private String description;

    /** 主图URL */
    @Excel(name = "主图URL")
    private String image;

    /** 菜谱分类（字符串或分类ID） */
    @Excel(name = "菜谱分类", readConverterExp = "字=符串或分类ID")
    private String category;

    /** 准备时间（分钟） */
    @Excel(name = "准备时间", readConverterExp = "分=钟")
    private Long prepTime;

    /** 烹饪时间（分钟） */
    @Excel(name = "烹饪时间", readConverterExp = "分=钟")
    private Long cookTime;

    /** 创建者用户ID，关联 sys_user.user_id */
    @Excel(name = "创建者用户ID，关联 sys_user.user_id")
    private Long createdBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public void setPrepTime(Long prepTime) 
    {
        this.prepTime = prepTime;
    }

    public Long getPrepTime() 
    {
        return prepTime;
    }

    public void setCookTime(Long cookTime) 
    {
        this.cookTime = cookTime;
    }

    public Long getCookTime() 
    {
        return cookTime;
    }

    public void setCreatedBy(Long createdBy) 
    {
        this.createdBy = createdBy;
    }

    public Long getCreatedBy() 
    {
        return createdBy;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("description", getDescription())
            .append("image", getImage())
            .append("category", getCategory())
            .append("prepTime", getPrepTime())
            .append("cookTime", getCookTime())
            .append("createdBy", getCreatedBy())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
