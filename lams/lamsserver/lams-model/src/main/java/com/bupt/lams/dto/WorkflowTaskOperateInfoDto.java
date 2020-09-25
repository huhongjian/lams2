package com.bupt.lams.dto;

import com.bupt.lams.constants.BusinessConstant;
import com.bupt.lams.model.Hr;
import com.bupt.lams.model.Role;
import com.bupt.lams.model.WorkflowOperate;

import java.util.ArrayList;
import java.util.List;

/**
 * 待办任务表单信息获取
 */
public class WorkflowTaskOperateInfoDto {

    private List<Integer> operateTypes = new ArrayList<>();
    private List<WorkflowOperate> operateList = new ArrayList<>();
    private List<Role> candidateGroups = new ArrayList<>();
    private List<Hr> candidateUsers = new ArrayList<>();
    private String operationHint = BusinessConstant.EMPTY_STR;

    public List<Integer> getOperateTypes() {
        return operateTypes;
    }

    public void setOperateTypes(List<Integer> operateTypes) {
        this.operateTypes = operateTypes;
    }

    public List<WorkflowOperate> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<WorkflowOperate> operateList) {
        this.operateList = operateList;
    }

    public List<Role> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<Role> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public List<Hr> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<Hr> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public String getOperationHint() {
        return operationHint;
    }

    public void setOperationHint(String operationHint) {
        this.operationHint = operationHint;
    }
}
