package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * 评论（支持@/回复/状态）对象 comments
 * 
 * @author Z
 * @date 2026-03-23
 */
public class Comments extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Long id;

    /** 资源类型：recipe/post/book等 */
    @Excel(name = "资源类型：recipe/post/book等")
    private String resourceType;

    /** 资源ID */
    @Excel(name = "资源ID")
    private String resourceId;

    /** 父评论ID（用于回复） */
    @Excel(name = "父评论ID", readConverterExp = "用=于回复")
    private Long parentId;

    /** 评论用户ID，关联 sys_user.user_id */
    @Excel(name = "评论用户ID，关联 sys_user.user_id")
    private Long userId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 被@用户ID数组 */
    @Excel(name = "被@用户ID数组")
    private String atUsers;

    /** 状态: visible/hidden/flagged */
    @Excel(name = "状态: visible/hidden/flagged")
    private String status;

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

    public void setResourceType(String resourceType) 
    {
        this.resourceType = resourceType;
    }

    public String getResourceType() 
    {
        return resourceType;
    }

    public void setResourceId(String resourceId) 
    {
        this.resourceId = resourceId;
    }

    public String getResourceId() 
    {
        return resourceId;
    }

    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setAtUsers(String atUsers) 
    {
        this.atUsers = atUsers;
    }

    public String getAtUsers() 
    {
        return atUsers;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("resourceType", getResourceType())
            .append("resourceId", getResourceId())
            .append("parentId", getParentId())
            .append("userId", getUserId())
            .append("content", getContent())
            .append("atUsers", getAtUsers())
            .append("status", getStatus())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
