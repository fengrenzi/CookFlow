package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * 菜谱聚合统计，用于排行榜对象 recipe_stats
 * 
 * @author Z
 * @date 2026-03-23
 */
public class RecipeStats extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 菜谱ID */
    private Long recipeId;

    /** 浏览量 */
    @Excel(name = "浏览量")
    private Long views;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likes;

    /** 收藏数 */
    @Excel(name = "收藏数")
    private Long favorites;

    /** 评论数 */
    @Excel(name = "评论数")
    private Long comments;

    /** 综合热度分 */
    @Excel(name = "综合热度分")
    private Long score;

    /** 统计更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastUpdated;

    public void setRecipeId(Long recipeId) 
    {
        this.recipeId = recipeId;
    }

    public Long getRecipeId() 
    {
        return recipeId;
    }

    public void setViews(Long views) 
    {
        this.views = views;
    }

    public Long getViews() 
    {
        return views;
    }

    public void setLikes(Long likes) 
    {
        this.likes = likes;
    }

    public Long getLikes() 
    {
        return likes;
    }

    public void setFavorites(Long favorites) 
    {
        this.favorites = favorites;
    }

    public Long getFavorites() 
    {
        return favorites;
    }

    public void setComments(Long comments) 
    {
        this.comments = comments;
    }

    public Long getComments() 
    {
        return comments;
    }

    public void setScore(Long score) 
    {
        this.score = score;
    }

    public Long getScore() 
    {
        return score;
    }

    public void setLastUpdated(Date lastUpdated) 
    {
        this.lastUpdated = lastUpdated;
    }

    public Date getLastUpdated() 
    {
        return lastUpdated;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recipeId", getRecipeId())
            .append("views", getViews())
            .append("likes", getLikes())
            .append("favorites", getFavorites())
            .append("comments", getComments())
            .append("score", getScore())
            .append("lastUpdated", getLastUpdated())
            .toString();
    }
}
