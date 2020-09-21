package com.sogou.admin.workorder.workflow.service.task;

import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormProperty;

import com.sogou.admin.workorder.workflow.dto.ExecutionDto;
import com.sogou.admin.workorder.workflow.dto.TaskDto;
import com.sogou.admin.workorder.workflow.dto.TaskQueryDto;

/**
 * Activiti Task 服务
 *
 * @author shanglonghua
 */
public interface TaskManagerService {

	/**
	 * 查询未签收任务（暂不支持分页）
	 *
	 * @param taskQueryDto
	 *            查询对象（支持按用户账号及业务Key精确查询）
	 * @return 待办任务集合
	 */
	List<TaskDto> getCandidateTasks(TaskQueryDto taskQueryDto);

	/**
	 * 查询全部任务（包括未签收和已签收，暂不支持分页）
	 *
     @param taskQueryDto
      *            查询对象（支持按用户账号及业务Key精确查询）
	 * @return 待办任务集合
	 */
	List<TaskDto> getCandidateOrAssignedTasks(TaskQueryDto taskQueryDto);

	/**
	 * 查询已签收任务（暂不支持分页）
	 *
	 * @param taskQueryDto
	 *            查询对象（只支持按账号查询）
	 * @return 已签收任务集合
	 */
	List<TaskDto> getAssignedTasks(TaskQueryDto taskQueryDto);

	/**
	 * 查询已签收任务总数
	 *
	 * @param taskQueryDto
	 *            查询对象（只支持按账号查询）
	 * @return 已签收任务总数
	 */
	Long getAssignedTasksCount(TaskQueryDto taskQueryDto);

	/**
	 * 查询未签收任务总数
	 *
	 * @param taskQueryDto
	 *            查询对象（只支持按账号查询）
	 * @return 未签收任务总数
	 */
	Long getCandidateTasksCount(TaskQueryDto taskQueryDto);

	/**
	 * 查询任务表单信息
	 *
	 * @param taskQueryDto
	 *            查询对象（taskId,userId)
	 * @return 任务表单信息
	 */
	List<FormProperty> getCandidateTaskFormData(TaskQueryDto taskQueryDto);

	/**
	 * 任务签收c
	 *
	 * @param taskId
	 *            任务ID
	 * @param userName
	 *            用户账号
	 * @param variablesMap
	 *            流程变量
	 */
	void claimTask(String taskId, String userName, Map variablesMap);

	/**
	 * 任务办理
	 *
	 * @param taskId
	 *            任务ID
	 * @param formValues
	 *            任务表单信息
	 * @param userId
	 *            用户账号
	 */
	void completeTask(String taskId, Map formValues, String userId);

	/**
	 * 根据流程实例ID及任务名称获取执行实例
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @param actId
	 *            执行实例ID
	 * @return 指定实例对象信息
	 */
	ExecutionDto getExecutionDtoByProcInstIdAndActId(String procInstId, String actId);

	/**
	 * 办理接收任务
	 *
	 * @param executionId
	 *            执行实例ID
	 * @param formValues
	 *            表单信息
	 */
	void triggerReceiveTask(String executionId, Map formValues);

}
