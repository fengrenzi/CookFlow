package com.hnit.system.domain;

import com.hnit.common.annotation.Excel;

/**
 * 书籍中页码到菜谱的映射对象 book_recipes
 * 
 * @author Z
 * @date 2026-03-23
 */
public class BookRecipes extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 书籍ID */
    private String bookId;

    /** 页码 */
    private Long pageNumber;

    /** 对应的菜谱ID（若存在） */
    @Excel(name = "对应的菜谱ID", readConverterExp = "若=存在")
    private Long recipeId;

    public void setBookId(String bookId) 
    {
        this.bookId = bookId;
    }

    public String getBookId() 
    {
        return bookId;
    }

    public void setPageNumber(Long pageNumber) 
    {
        this.pageNumber = pageNumber;
    }

    public Long getPageNumber() 
    {
        return pageNumber;
    }

    public void setRecipeId(Long recipeId) 
    {
        this.recipeId = recipeId;
    }

    public Long getRecipeId() 
    {
        return recipeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("bookId", getBookId())
            .append("pageNumber", getPageNumber())
            .append("recipeId", getRecipeId())
            .toString();
    }
}
