package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * AI 会话元信息对象 ai_conversations
 * 
 * @author Z
 * @date 2026-03-23
 */
public class AiConversations extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会话ID */
    private Long id;

    /** 发起会话的用户ID，关联 sys_user.user_id（若匿名可为空） */
    @Excel(name = "发起会话的用户ID，关联 sys_user.user_id", readConverterExp = "若=匿名可为空")
    private Long userId;

    /** 会话键，用于前端恢复会话 */
    @Excel(name = "会话键，用于前端恢复会话")
    private String sessionKey;

    /** 使用的模型名 */
    @Excel(name = "使用的模型名")
    private String model;

    /** 模型版本/配置 */
    @Excel(name = "模型版本/配置")
    private String modelVersion;

    /** 会话额外上下文（结构化） */
    @Excel(name = "会话额外上下文", readConverterExp = "结=构化")
    private String context;

    /** 会话摘要/要点（可供快速检索） */
    @Excel(name = "会话摘要/要点", readConverterExp = "可=供快速检索")
    private String summary;

    /** 最近活跃时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最近活跃时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastActiveAt;

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

    public void setSessionKey(String sessionKey) 
    {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() 
    {
        return sessionKey;
    }

    public void setModel(String model) 
    {
        this.model = model;
    }

    public String getModel() 
    {
        return model;
    }

    public void setModelVersion(String modelVersion) 
    {
        this.modelVersion = modelVersion;
    }

    public String getModelVersion() 
    {
        return modelVersion;
    }

    public void setContext(String context) 
    {
        this.context = context;
    }

    public String getContext() 
    {
        return context;
    }

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    public void setLastActiveAt(Date lastActiveAt) 
    {
        this.lastActiveAt = lastActiveAt;
    }

    public Date getLastActiveAt() 
    {
        return lastActiveAt;
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
            .append("sessionKey", getSessionKey())
            .append("model", getModel())
            .append("modelVersion", getModelVersion())
            .append("context", getContext())
            .append("summary", getSummary())
            .append("lastActiveAt", getLastActiveAt())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
