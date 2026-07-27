package com.hnit.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnit.common.annotation.Excel;

/**
 * 购物车项对象 cart_items
 * 
 * @author Z
 * @date 2026-03-23
 */
public class CartItems extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 购物项ID */
    private Long id;

    /** 所属用户ID，关联 sys_user.user_id */
    @Excel(name = "所属用户ID，关联 sys_user.user_id")
    private Long userId;

    /** 项类型：菜谱或书中菜谱 */
    @Excel(name = "项类型：菜谱或书中菜谱")
    private String type;

    /** 关联的 recipe id 或 book id */
    @Excel(name = "关联的 recipe id 或 book id")
    private String relatedId;

    /** 数量 */
    @Excel(name = "数量")
    private Long quantity;

    /** 可选：具体食材列表（含数量） */
    @Excel(name = "可选：具体食材列表", readConverterExp = "含=数量")
    private String ingredients;

    /** 添加时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "添加时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date addedAt;

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

    public void setRelatedId(String relatedId) 
    {
        this.relatedId = relatedId;
    }

    public String getRelatedId() 
    {
        return relatedId;
    }

    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }

    public void setIngredients(String ingredients) 
    {
        this.ingredients = ingredients;
    }

    public String getIngredients() 
    {
        return ingredients;
    }

    public void setAddedAt(Date addedAt) 
    {
        this.addedAt = addedAt;
    }

    public Date getAddedAt() 
    {
        return addedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("type", getType())
            .append("relatedId", getRelatedId())
            .append("quantity", getQuantity())
            .append("ingredients", getIngredients())
            .append("addedAt", getAddedAt())
            .toString();
    }
}
