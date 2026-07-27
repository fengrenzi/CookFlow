package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * AI 会话消息（按消息存储）对象 ai_messages
 * 
 * @author hnit
 * @date 2026-03-23
 */
public class AiMessages extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long id;

    /** 所属会话ID */
    @Excel(name = "所属会话ID")
    private Long conversationId;

    /** sender: user/system/assistant */
    @Excel(name = "sender: user/system/assistant")
    private String sender;

    /** 文本内容（若为语音已转写） */
    @Excel(name = "文本内容", readConverterExp = "若=为语音已转写")
    private String content;

    /** text/audio/image等 */
    @Excel(name = "text/audio/image等")
    private String contentType;

    /** 语音识别替换结果、语速、置信度等 */
    @Excel(name = "语音识别替换结果、语速、置信度等")
    private String metadata;

    /** 供计费/分析使用的token数 */
    @Excel(name = "供计费/分析使用的token数")
    private Long tokenCount;

    /** 消息时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "消息时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setConversationId(Long conversationId) 
    {
        this.conversationId = conversationId;
    }

    public Long getConversationId() 
    {
        return conversationId;
    }

    public void setSender(String sender) 
    {
        this.sender = sender;
    }

    public String getSender() 
    {
        return sender;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setContentType(String contentType) 
    {
        this.contentType = contentType;
    }

    public String getContentType() 
    {
        return contentType;
    }

    public void setMetadata(String metadata) 
    {
        this.metadata = metadata;
    }

    public String getMetadata() 
    {
        return metadata;
    }

    public void setTokenCount(Long tokenCount) 
    {
        this.tokenCount = tokenCount;
    }

    public Long getTokenCount() 
    {
        return tokenCount;
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
            .append("conversationId", getConversationId())
            .append("sender", getSender())
            .append("content", getContent())
            .append("contentType", getContentType())
            .append("metadata", getMetadata())
            .append("tokenCount", getTokenCount())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
