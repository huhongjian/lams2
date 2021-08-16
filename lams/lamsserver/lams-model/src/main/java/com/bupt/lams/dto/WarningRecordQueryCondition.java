package com.bupt.lams.dto;

import lombok.Data;

import java.util.Date;

/**
 * 预算报警记录查询条件
 */
@Data
public class WarningRecordQueryCondition {
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

    /**
     * 科研项目信息
     */
    private String projectName;
    private String projectType;
    private String userEmail;
}
