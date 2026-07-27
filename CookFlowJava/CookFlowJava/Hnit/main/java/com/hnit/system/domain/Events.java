package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * 用户行为事件（事件流）对象 events
 * 
 * @author Z
 * @date 2026-03-23
 */
public class Events extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事件ID */
    private Long id;

    /** 触发事件的用户ID，关联 sys_user.user_id */
    @Excel(name = "触发事件的用户ID，关联 sys_user.user_id")
    private Long userId;

    /** 事件类型：view/click/favorite/add_to_cart/purchase等 */
    @Excel(name = "事件类型：view/click/favorite/add_to_cart/purchase等")
    private String eventType;

    /** 资源类型 */
    @Excel(name = "资源类型")
    private String resourceType;

    /** 资源ID */
    @Excel(name = "资源ID")
    private String resourceId;

    /** 扩展属性，如 device/referrer */
    @Excel(name = "扩展属性，如 device/referrer")
    private String properties;

    /** 事件时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "事件时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setEventType(String eventType) 
    {
        this.eventType = eventType;
    }

    public String getEventType() 
    {
        return eventType;
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

    public void setProperties(String properties) 
    {
        this.properties = properties;
    }

    public String getProperties() 
    {
        return properties;
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
            .append("userId", getUserId())
            .append("eventType", getEventType())
            .append("resourceType", getResourceType())
            .append("resourceId", getResourceId())
            .append("properties", getProperties())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
