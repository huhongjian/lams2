package com.bupt.workflow.dto;

import java.util.Date;

/**
 * 流程DTO
 */
public class ProcessDto {

	/**
	 * 流程实例ID
	 */
	private String procInstId;

	/**
	 * 业务标识Key
	 */
	private String businessKey;

	/**
	 * 流程开始时间
	 */
	private Date startTime;

	/**
	 * 流程结束时间
	 */
	private Date endTime;

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
