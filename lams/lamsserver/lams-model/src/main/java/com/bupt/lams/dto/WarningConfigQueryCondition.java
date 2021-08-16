package com.bupt.lams.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 预算报警信息查询条件
 */
@Data
public class WarningConfigQueryCondition {
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
     * 预算报警信息
     */
    private Integer freq;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date currentDate;
    private String userEmail;
}
