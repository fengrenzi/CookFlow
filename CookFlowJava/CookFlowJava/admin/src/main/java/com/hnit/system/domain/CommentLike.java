package com.hnit.system.domain;

import lombok.Data;
import java.util.Date;

@Data
public class CommentLike {
    private String commentId;
    private Long userId;
    private Date createdAt;
}