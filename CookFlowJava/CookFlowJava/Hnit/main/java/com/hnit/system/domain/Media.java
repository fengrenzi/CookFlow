package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * 媒体资源（图片/视频/音频）对象 media
 * 
 * @author Z
 * @date 2026-03-23
 */
public class Media extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 媒体ID */
    private Long id;

    /** 上传者用户ID，关联 sys_user.user_id */
    @Excel(name = "上传者用户ID，关联 sys_user.user_id")
    private Long ownerId;

    /** 媒体类型：image/video/audio */
    @Excel(name = "媒体类型：image/video/audio")
    private String type;

    /** 存储/访问URL */
    @Excel(name = "存储/访问URL")
    private String url;

    /** 云存储Key */
    @Excel(name = "云存储Key")
    private String storageKey;

    /** MIME类型 */
    @Excel(name = "MIME类型")
    private String mime;

    /** 字节大小 */
    @Excel(name = "字节大小")
    private Long size;

    /** 缩略图URL（如果有） */
    @Excel(name = "缩略图URL", readConverterExp = "如=果有")
    private String thumbUrl;

    /** 额外元数据（宽高、时长等） */
    @Excel(name = "额外元数据", readConverterExp = "宽=高、时长等")
    private String metadata;

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

    public void setOwnerId(Long ownerId) 
    {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() 
    {
        return ownerId;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

    public void setStorageKey(String storageKey) 
    {
        this.storageKey = storageKey;
    }

    public String getStorageKey() 
    {
        return storageKey;
    }

    public void setMime(String mime) 
    {
        this.mime = mime;
    }

    public String getMime() 
    {
        return mime;
    }

    public void setSize(Long size) 
    {
        this.size = size;
    }

    public Long getSize() 
    {
        return size;
    }

    public void setThumbUrl(String thumbUrl) 
    {
        this.thumbUrl = thumbUrl;
    }

    public String getThumbUrl() 
    {
        return thumbUrl;
    }

    public void setMetadata(String metadata) 
    {
        this.metadata = metadata;
    }

    public String getMetadata() 
    {
        return metadata;
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
            .append("ownerId", getOwnerId())
            .append("type", getType())
            .append("url", getUrl())
            .append("storageKey", getStorageKey())
            .append("mime", getMime())
            .append("size", getSize())
            .append("thumbUrl", getThumbUrl())
            .append("metadata", getMetadata())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
