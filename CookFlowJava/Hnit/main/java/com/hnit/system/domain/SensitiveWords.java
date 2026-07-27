package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * 敏感词库，用于内容审核/替换对象 sensitive_words
 * 
 * @author Z
 * @date 2026-03-23
 */
public class SensitiveWords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 敏感词ID */
    private Long id;

    /** 敏感词 */
    @Excel(name = "敏感词")
    private String word;

    /** 分类，如 profanity/politics */
    @Excel(name = "分类，如 profanity/politics")
    private String category;

    /** 替换词（可为空） */
    @Excel(name = "替换词", readConverterExp = "可=为空")
    private String replaceWith;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setWord(String word) 
    {
        this.word = word;
    }

    public String getWord() 
    {
        return word;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public void setReplaceWith(String replaceWith) 
    {
        this.replaceWith = replaceWith;
    }

    public String getReplaceWith() 
    {
        return replaceWith;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("word", getWord())
            .append("category", getCategory())
            .append("replaceWith", getReplaceWith())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
