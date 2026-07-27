package com.hnit.system.domain.vo;

import com.hnit.common.core.domain.entity.SysUser;
import com.hnit.system.domain.ImagesRecord;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CommentVo {
    private String id;
    private SysUser user;               // 原始用户对象，avatar 为图片ID
    private String avatarUrl;           // 完整头像URL（由Service填充）
    private String content;
    private Integer rating;
    private Integer likes;
    private Boolean isLiked;
    private Date createdAt;
    private List<ImagesRecord> images;   // 原始图片记录，imageId 为图片ID
    private List<String> imageUrls;      // 完整图片URL列表（由Service填充）
    private List<CommentReplyVo> replies;
    private Integer replyCount;
}