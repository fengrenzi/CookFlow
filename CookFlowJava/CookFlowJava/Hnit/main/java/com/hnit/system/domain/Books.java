package com.hnit.system.domain;

import com.hnit.common.annotation.Excel;

/**
 * 书籍对象 books
 * 
 * @author Z
 * @date 2026-03-23
 */
public class Books extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 书籍ID */
    private String id;

    /** 书名 */
    @Excel(name = "书名")
    private String title;

    /** 作者 */
    @Excel(name = "作者")
    private String author;

    /** 书籍元数据 */
    @Excel(name = "书籍元数据")
    private String metadata;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setAuthor(String author) 
    {
        this.author = author;
    }

    public String getAuthor() 
    {
        return author;
    }

    public void setMetadata(String metadata) 
    {
        this.metadata = metadata;
    }

    public String getMetadata() 
    {
        return metadata;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("author", getAuthor())
            .append("metadata", getMetadata())
            .toString();
    }
}
