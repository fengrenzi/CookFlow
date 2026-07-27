package com.hnit.system.domain;

import lombok.Data;
import java.util.Date;

@Data
public class Comment {
    private String id;
    private String resourceType;    // 'recipe', 'book', 'activity', 'forum'
    private String resourceId;
    private String parentId;
    private Long userId;
    private String content;
    private String atUsers;         // JSON 字符串
    private Integer rating;
    private Integer likes;
    private Integer replyCount;
    private String status;          // 'visible', 'hidden', 'deleted'
    private Date createdAt;
    private Date updatedAt;
}