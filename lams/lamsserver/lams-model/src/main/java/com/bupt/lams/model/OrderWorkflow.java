package com.bupt.lams.model;

import lombok.Data;

import java.util.Date;

/**
 * 工单和工作流实例的关系
 */
@Data
public class OrderWorkflow {
    private Long id;

    private Long oid;

    private Long workflowInstId;

    private Date workflowStartTime;
}
