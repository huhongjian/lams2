package com.sogou.admin.workorder.workflow.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务Dto
 *
 * @author shanglonghua
 */
public class TaskDto {

	/**
	 * 任务实例ID
	 */
	private String taskId;
	/**
	 * 任务名称（与bpmn文件中的一致）
	 */
	private String name;
	/**
	 * 所属类别（与bpmn文件中的一致）
	 */
	private String category;
	/**
	 * 任务定义key（与bpmn文件中的一致）
	 */
	private String taskDefinitionKey;
	/**
	 * 办理人
	 */
	private String assignee;
	/**
	 * 任务创建时间
	 */
	private String createTime;
	/**
	 * 任务签收时间
	 */
	private String claimTime;
	/**
	 * 任务结束时间
	 */
	private String endTime;
	/**
	 * 流程实例ID
	 */
	private String processInstanceId;
	/**
	 * 流程实例业务Key(与processInstanceId一对一)
	 */
	private String businessKey;
	/**
	 * 候选待办用户
	 */
	private List<String> candidateUserIds;
	/**
	 * 候选待办组
	 */
	private List<String> candidateGroupIds;
	/**
	 * 流程变量
	 */
	private Map<String, Object> variablesMap = new HashMap<String, Object>();

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(String claimTime) {
		this.claimTime = claimTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public List<String> getCandidateUserIds() {
		return candidateUserIds;
	}

	public void setCandidateUserIds(List<String> candidateUserIds) {
		this.candidateUserIds = candidateUserIds;
	}

	public List<String> getCandidateGroupIds() {
		return candidateGroupIds;
	}

	public void setCandidateGroupIds(List<String> candidateGroupIds) {
		this.candidateGroupIds = candidateGroupIds;
	}

	public Map<String, Object> getVariablesMap() {
		return variablesMap;
	}

	public void setVariablesMap(Map<String, Object> variablesMap) {
		this.variablesMap = variablesMap;
	}
}
