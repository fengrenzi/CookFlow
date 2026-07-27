package com.hnit.system.domain.vo;

import lombok.Data;
import java.util.Date;

@Data
public class ReplyVO {
    private String id;
    private String content;
    private Date createTime;
    private String sourceType;
    private String sourceId;
    private String sourceTitle;
    private Boolean isRead;
}