package com.hnit.system.domain;

import com.hnit.common.annotation.Excel;

/**
 * 食材对象 ingredients
 * 
 * @author Z
 * @date 2026-03-23
 */
public class Ingredients extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 食材ID，自定义或组合ID（如 a-1） */
    private String id;

    /** 食材名称 */
    @Excel(name = "食材名称")
    private String name;

    /** 食材图片URL */
    @Excel(name = "食材图片URL")
    private String imgUrl;

    /** 食材分类ID或名称 */
    @Excel(name = "食材分类ID或名称")
    private String category;

    /** 字母分组（首字母） */
    @Excel(name = "字母分组", readConverterExp = "首=字母")
    private String letter;

    /** 默认计量单位（如 g、个） */
    @Excel(name = "默认计量单位", readConverterExp = "如=,g=、个")
    private String unit;

    /** 扩展字段，存放额外元信息 */
    @Excel(name = "扩展字段，存放额外元信息")
    private String extra;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
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

    public void setImgUrl(String imgUrl) 
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() 
    {
        return imgUrl;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public void setLetter(String letter) 
    {
        this.letter = letter;
    }

    public String getLetter() 
    {
        return letter;
    }

    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }

    public void setExtra(String extra) 
    {
        this.extra = extra;
    }

    public String getExtra() 
    {
        return extra;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("imgUrl", getImgUrl())
            .append("category", getCategory())
            .append("letter", getLetter())
            .append("unit", getUnit())
            .append("extra", getExtra())
            .toString();
    }
}
