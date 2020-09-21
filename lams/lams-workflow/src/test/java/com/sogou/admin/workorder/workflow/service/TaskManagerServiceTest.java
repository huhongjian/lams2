package com.sogou.admin.workorder.workflow.service;

import com.sogou.admin.workorder.workflow.dto.ExecutionDto;
import com.sogou.admin.workorder.workflow.dto.TaskDto;
import com.sogou.admin.workorder.workflow.dto.TaskQueryDto;
import com.sogou.admin.workorder.workflow.service.task.TaskManagerService;
import main.WorkflowApplication;
import org.activiti.engine.form.FormProperty;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TaskManagerService单测
 * 
 * @author shanglonghua
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WorkflowApplication.class })
public class TaskManagerServiceTest {

	@Resource
	TaskManagerService taskManagerService;

	private static TaskQueryDto taskQueryDto;

	static {
		taskQueryDto = new TaskQueryDto();
		taskQueryDto.setUserName("zengmin@sogou-inc.com");
	}

	@Test
	public void testGetCandidateTasks() {
		List<TaskDto> taskList = this.taskManagerService.getCandidateTasks(taskQueryDto);

		// 未签收任务
		Long candidateTasksCount = this.taskManagerService.getCandidateTasksCount(taskQueryDto);
		Assert.assertEquals(Long.valueOf(0), candidateTasksCount);

		// 已签收任务
		Long assignedTasksCount = this.taskManagerService.getAssignedTasksCount(taskQueryDto);
		Assert.assertEquals(Long.valueOf(1), assignedTasksCount);

	}
	
	@Test
	@Ignore
	public void testClaimTask() {
		// 签收任务
		this.taskManagerService.claimTask("10006", "zengmin@sogou-inc.com",null);
	}

	@Test
	public void testHandleTask() {
		// 获取待办任务
		List<TaskDto> taskDtos = this.taskManagerService.getAssignedTasks(taskQueryDto);

		TaskDto taskDto = taskDtos.get(0);

		// 办理任务（回复代理商）
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("handleType", "2");

		this.taskManagerService.completeTask(taskDto.getTaskId(), paramMap, taskQueryDto.getUserName());

	}

	@Test
	public void testValidateTask() {
		ExecutionDto executionDto = this.taskManagerService.getExecutionDtoByProcInstIdAndActId(
		        "2501", "agentValidateRecTask");
		Map<String,String> paramsMap = new HashMap<String,String>();
		paramsMap.put("validateResult", "false");
		this.taskManagerService.triggerReceiveTask(executionDto.getExecutionId(), paramsMap);
	}

	@Test
	public void testGetCandidateTaskFormData() {
		taskQueryDto.setTaskId("20004");
		List<FormProperty> formData = this.taskManagerService.getCandidateTaskFormData(taskQueryDto);
		((HashMap) formData.get(0).getType().getInformation("values")).keySet();
		// JSONObject jsonObject = JSONObject.parseObject(formData);
		// jsonObject.get("candidateGroups");
		formData.size();

	}

}
