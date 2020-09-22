package com.bupt.workflow.dto;

/**
 * 任务查询DTO
 *
 * @author shanglonghua
 */
public class TaskQueryDto extends QueryBaseDto {

	private String taskId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
