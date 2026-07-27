package com.hnit.system.domain;// Region.java

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Region {
    private String id;          // UUID
    private String code;
    private String name;
    private String parentCode;
    private Integer level;
    private BigDecimal lng;
    private BigDecimal lat;
    private Date createTime;
}