package com.hnit.system.domain;

import com.hnit.common.annotation.Excel;

/**
 * 标签映射对象 tag_map
 * 
 * @author Z
 * @date 2026-03-23
 */
public class TagMap extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标签ID */
    private Long tagId;

    /** 资源类型 */
    private String resourceType;

    /** 资源ID */
    private String resourceId;

    public void setTagId(Long tagId) 
    {
        this.tagId = tagId;
    }

    public Long getTagId() 
    {
        return tagId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("tagId", getTagId())
            .append("resourceType", getResourceType())
            .append("resourceId", getResourceId())
            .toString();
    }
}
