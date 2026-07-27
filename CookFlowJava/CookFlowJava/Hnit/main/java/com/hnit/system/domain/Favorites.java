package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * 用户收藏对象 favorites
 * 
 * @author Z
 * @date 2026-03-23
 */
public class Favorites extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID，关联 sys_user.user_id */
    private Long userId;

    /** 被收藏的菜谱ID */
    private Long recipeId;

    /** 收藏时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "收藏时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setRecipeId(Long recipeId) 
    {
        this.recipeId = recipeId;
    }

    public Long getRecipeId() 
    {
        return recipeId;
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
            .append("userId", getUserId())
            .append("recipeId", getRecipeId())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
