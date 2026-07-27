package com.hnit.system.domain.vo;

import lombok.Data;

@Data
public class BookSimpleVO {
    private String id;
    private String title;
    private String author;
    private String coverUrl;
    private String publishDate;   // 上架日期
    private String collectDate;   // 收藏日期
    private String lastRead;      // 最后阅读时间
    private Integer progress;     // 阅读进度
}