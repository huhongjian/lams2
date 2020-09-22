package com.bupt.workflow.dto;

import java.util.List;

/**
 * Activiti 任务查询DTO
 */
public abstract class QueryBaseDto {

	// 用户ID
	private String userName;
	// 业务编号
	private String businessKey;
	// 流程实例ID
	private List<String> procInstIds;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public List<String> getProcInstIds() {
		return procInstIds;
	}

	public void setProcInstIds(List<String> procInstIds) {
		this.procInstIds = procInstIds;
	}
}
