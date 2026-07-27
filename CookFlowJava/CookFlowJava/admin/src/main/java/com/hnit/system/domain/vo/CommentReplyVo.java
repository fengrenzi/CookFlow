package com.hnit.system.domain.vo;

import com.hnit.common.core.domain.entity.SysUser;
import lombok.Data;
import java.util.Date;

@Data
public class CommentReplyVo {
    private String id;
    private SysUser user;               // 原始用户对象
    private String avatarUrl;           // 完整头像URL（由Service填充）
    private String content;
    private Date createdAt;
    private Integer likes;
    private Boolean isLiked;
}