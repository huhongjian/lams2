package com.bupt.lams.model;

import lombok.Data;

import java.util.Date;

/**
 * 流程类型和工作流key的映射
 */
@Data
public class ProcessTypeWorkflow {
    private Long id;

    private Integer category;

    private String categoryName;

    private String workflowKey;

    private String creator;

    private Date createTime;
}
