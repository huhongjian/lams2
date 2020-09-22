package com.bupt.workflow.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行实例DTO
 */
public class ExecutionDto {

	/**
	 * 执行实例ID
	 */
	private String executionId;
	/**
	 * 执行实例名称（与bpmn文件中的一致）
	 */
	private String name;
	/**
	 * 流程变量
	 */
	private Map<String, Object> variablesMap = new HashMap<String, Object>();

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getName() {
		return name;
	}

	public Map<String, Object> getVariablesMap() {
		return variablesMap;
	}

	public void setVariablesMap(Map<String, Object> variablesMap) {
		this.variablesMap = variablesMap;
	}

	public void setName(String name) {
		this.name = name;
	}

}
