package com.bupt.lams.dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * 工单查询条件
 */
@Data
public class OrderQueryCondition {
    private Long oid;
    /**
     * 工单相关信息
     */
    private Integer category;
    private Integer status;
    private String userEmail;
    private String reason;
    /**
     * 分页信息
     */
    private Integer page = 1;
    private Integer size = 10;
    /**
     * ID列表，用于批量查询
     */
    private Set<Long> ids;
    /**
     * 时间范围
     */
    private Date startDate;
    private Date endDate;
}
