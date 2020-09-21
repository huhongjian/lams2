package com.sogou.admin.workorder.workflow.convertor;

import com.sogou.admin.workorder.workflow.dto.TaskDto;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TaskDto转换器
 *
 * @author shanglonghua
 */
public class TaskDtoConvertor {

	/**
	 * Activiti Task实体转换为DTO
	 *
	 * @param taskList
	 *            任务对象列表
	 * @param procInstIdToBizKeyMap
	 *            实例与Bizkey映射
	 * @param taskVariablesMap
	 *            任务流程变量列表
	 * @return 任务DTO列表
	 */
	public static List<TaskDto> convertTaskToDtoList(List<Task> taskList, Map<String, String> procInstIdToBizKeyMap,
	        Map<String, Map<String, Object>> taskVariablesMap) {
		List<TaskDto> taskDtoList = new ArrayList<TaskDto>();
		if (taskList != null) {
			TaskDto tmpTaskDto;
			for (Task task : taskList) {
				tmpTaskDto = convertTaskToDto(task,procInstIdToBizKeyMap,taskVariablesMap);
				taskDtoList.add(tmpTaskDto);
			}
		}
		return taskDtoList;
	}

	/**
	 * Activiti Task实体转换为DTO
	 *
	 * @param task
	 *            任务对象
	 * @param procInstIdToBizKeyMap
	 *            实例与Bizkey映射
	 * @param taskVariablesMap
	 *            任务流程变量列表
	 * @return 任务DTO
	 */
	public static TaskDto convertTaskToDto(Task task, Map<String, String> procInstIdToBizKeyMap,
	        Map<String, Map<String, Object>> taskVariablesMap) {
		TaskDto tmpTaskDto = new TaskDto();
		if (task.getAssignee() != null) {
			tmpTaskDto.setAssignee(task.getAssignee());
		}
		tmpTaskDto.setTaskId(task.getId());
		tmpTaskDto.setName(task.getName());
		tmpTaskDto.setBusinessKey(procInstIdToBizKeyMap.get(task.getProcessInstanceId()));
		if (taskVariablesMap.containsKey(task.getId())) {
			tmpTaskDto.setVariablesMap(taskVariablesMap.get(task.getId()));
		}
		return tmpTaskDto;
	}

}
