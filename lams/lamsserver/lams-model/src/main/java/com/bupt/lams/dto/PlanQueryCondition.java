package com.bupt.lams.dto;

import lombok.Data;

import java.util.Date;

/**
 * 预算计划信息查询条件
 */
@Data
public class PlanQueryCondition {
    private Long planId;
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

    /**
     * 科研项目信息
     */
    private String projectId;

    /**
     * 预算计划信息
     */
    private String planName;
    private String userEmail;
}
