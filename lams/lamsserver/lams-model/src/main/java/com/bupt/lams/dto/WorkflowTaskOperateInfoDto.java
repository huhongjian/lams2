package com.bupt.lams.dto;

import com.bupt.lams.model.WorkflowOperate;

import java.util.ArrayList;
import java.util.List;

/**
 * 待办任务表单信息获取
 */
public class WorkflowTaskOperateInfoDto {
    private List<WorkflowOperate> operateList = new ArrayList<>();
    private String candidateUser;

    public List<WorkflowOperate> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<WorkflowOperate> operateList) {
        this.operateList = operateList;
    }

    public String getCandidateUser() {
        return candidateUser;
    }

    public void setCandidateUser(String candidateUser) {
        this.candidateUser = candidateUser;
    }
}
