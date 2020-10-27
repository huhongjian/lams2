package com.bupt.lams.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RecordQueryCondition {
    private Long id;
    private Integer operate;
    private String operatorMail;
    private String text;
    /**
     * 分页信息
     */
    private Integer page = 1;
    private Integer size = 10;
    /**
     * 时间范围
     */
    private Date startDate;
    private Date endDate;
}
