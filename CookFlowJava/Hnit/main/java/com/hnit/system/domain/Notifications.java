package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * 用户通知对象 notifications
 * 
 * @author Z
 * @date 2026-03-23
 */
public class Notifications extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 通知ID */
    private Long id;

    /** 接收用户ID，关联 sys_user.user_id */
    @Excel(name = "接收用户ID，关联 sys_user.user_id")
    private Long userId;

    /** 通知类型 */
    @Excel(name = "通知类型")
    private String type;

    /** 通知负载，包含跳转信息 */
    @Excel(name = "通知负载，包含跳转信息")
    private String payload;

    /** 是否已读 */
    @Excel(name = "是否已读")
    private Integer isRead;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
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

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setPayload(String payload) 
    {
        this.payload = payload;
    }

    public String getPayload() 
    {
        return payload;
    }

    public void setIsRead(Integer isRead) 
    {
        this.isRead = isRead;
    }

    public Integer getIsRead() 
    {
        return isRead;
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
            .append("type", getType())
            .append("payload", getPayload())
            .append("isRead", getIsRead())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
