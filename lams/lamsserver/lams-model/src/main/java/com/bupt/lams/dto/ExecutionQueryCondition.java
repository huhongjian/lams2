package com.bupt.lams.dto;

import lombok.Data;

import java.util.Date;

/**
 * 预算执行流水信息查询条件
 */
@Data
public class ExecutionQueryCondition {
    private Long id;
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

    private String planName;
    private Long projectId;
    private String userEmail;
    private Long planItemId;
    private Integer itemType;
}
