package com.bupt.lams.dto;

import java.util.Date;
import java.util.List;

/**
 * 资产流转处理DTO
 */
public class TaskHandleDto {
    private Long id;
    private String operator;
    private Integer operateType;
    private String operate;
    private Date operateTime;
    private List<Integer> candidateGroups;
    private List<String> candidateUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public List<Integer> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<Integer> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public List<String> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

}
