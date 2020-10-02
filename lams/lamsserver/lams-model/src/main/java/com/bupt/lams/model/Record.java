package com.bupt.lams.model;

import lombok.Data;

import java.util.Date;

/**
 * 操作记录
 */
@Data
public class Record {
    private Long id;

    private Long oid;

    private Integer type;

    private String operator;

    private String operatorMail;

    private Integer operateType;

    private String operate;

    private Date operateTime;
}
