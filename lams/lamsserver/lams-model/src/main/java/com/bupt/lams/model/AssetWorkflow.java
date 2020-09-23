package com.bupt.lams.model;

import java.util.Date;

/**
 * 资产和工作流实例的关系
 */
public class AssetWorkflow {
    private Long id;

    private Long aid;

    private Long workflowInstId;

    private Date workflowStartTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Long getWorkflowInstId() {
        return workflowInstId;
    }

    public void setWorkflowInstId(Long workflowInstId) {
        this.workflowInstId = workflowInstId;
    }

    public Date getWorkflowStartTime() {
        return workflowStartTime;
    }

    public void setWorkflowStartTime(Date workflowStartTime) {
        this.workflowStartTime = workflowStartTime;
    }
}
