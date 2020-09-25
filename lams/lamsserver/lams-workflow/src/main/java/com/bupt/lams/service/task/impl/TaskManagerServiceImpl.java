package com.bupt.lams.service.task.impl;

import com.bupt.lams.convertor.TaskDtoConvertor;
import com.bupt.lams.dto.ExecutionDto;
import com.bupt.lams.dto.TaskDto;
import com.bupt.lams.dto.TaskQueryDto;
import com.bupt.lams.service.process.ProcessManagerService;
import com.bupt.lams.service.task.TaskManagerService;
import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Activiti 任务相关service实现类
 *
 * @author shanglonghua
 */
@Service
public class TaskManagerServiceImpl implements TaskManagerService {

    private Logger logger = LoggerFactory.getLogger(TaskManagerServiceImpl.class);

    @Resource
    private TaskService taskService;

    @Resource
    private FormService formService;

    @Resource
    private ProcessManagerService processManagerService;

    @Resource
    private RuntimeService runtimeService;

    @Override
    public List<TaskDto> getCandidateTasks(TaskQueryDto taskQueryDto) {
        logger.info("[start]获取未签收任务：" + taskQueryDto.getUserName() + "; 业务key : " + taskQueryDto.getBusinessKey());
        Long startTs = System.currentTimeMillis();
        List<TaskDto> retCandidateList = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery();
        // 1. 设置候选人
        if (StringUtils.isNotEmpty(taskQueryDto.getUserName())) {
            taskQuery.taskCandidateUser(taskQueryDto.getUserName()).active();
        }
        // 2. 设置BusinessKey（精确匹配）
        if (StringUtils.isNotEmpty(taskQueryDto.getBusinessKey())) {
            taskQuery.processInstanceBusinessKey(taskQueryDto.getBusinessKey());
        }
        // 3. 设置流程实例ID集合
        if (CollectionUtils.isNotEmpty(taskQueryDto.getProcInstIds())) {
            taskQuery.processInstanceIdIn(taskQueryDto.getProcInstIds());
        }
        taskQuery.active();
        // 4. 按时间升序排列
        List<Task> taskList = taskQuery.orderByTaskCreateTime().asc().list();
        if (CollectionUtils.isNotEmpty(taskList)) {
            // 5. 获取businessKey
            Map<String, String> procInstIdToBizKeyMap = processManagerService
                    .getProcBizKeyMapById(getProcInstIds(taskList));
            for (Task task : taskList) {
                TaskDto taskDto = TaskDtoConvertor.convertTaskToDto(task, procInstIdToBizKeyMap,
                        Collections.EMPTY_MAP);
                this.fillCandiateInfo(taskDto, task);
                retCandidateList.add(taskDto);
            }

        }
        logger.info("[end]获取未签收任务：" + taskQueryDto.getUserName() + "; 业务key : " + taskQueryDto.getBusinessKey() + ",耗时："
                + (System.currentTimeMillis() - startTs));
        return retCandidateList;
    }

    @Override
    public List<TaskDto> getCandidateOrAssignedTasks(TaskQueryDto taskQueryDto) {
        logger.info("[start]获取全部任务：" + taskQueryDto.getUserName() + "; 业务key : " + taskQueryDto.getBusinessKey());
        Long startTs = System.currentTimeMillis();
        List<TaskDto> retCandidateList = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery();
        // 1. 设置候选人
        if (StringUtils.isNotEmpty(taskQueryDto.getUserName())) {
            taskQuery.taskCandidateOrAssigned(taskQueryDto.getUserName()).active();
        }
        // 2. 设置BusinessKey（精确匹配）
        if (StringUtils.isNotEmpty(taskQueryDto.getBusinessKey())) {
            taskQuery.processInstanceBusinessKey(taskQueryDto.getBusinessKey());
        }
        // 3. 设置流程实例ID集合
        if (!taskQueryDto.getProcInstIds().isEmpty()) {
            taskQuery.processInstanceIdIn(taskQueryDto.getProcInstIds());
        }
        taskQuery.active();
        // 4. 按时间升序排列
        List<Task> taskList = taskQuery.orderByTaskCreateTime().asc().list();
        if (!taskList.isEmpty()) {
            // 5. 获取businessKey
            Map<String, String> procInstIdToBizKeyMap = processManagerService
                    .getProcBizKeyMapById(getProcInstIds(taskList));
            Map<String, Map<String, Object>> taskVariablesMap = this.getProcessVariables(taskList);
            for (Task task : taskList) {
                TaskDto taskDto = TaskDtoConvertor.convertTaskToDto(task, procInstIdToBizKeyMap,
                        taskVariablesMap);
                this.fillCandiateInfo(taskDto, task);
                retCandidateList.add(taskDto);
            }

        }
        logger.info("[end]获取全部任务：" + taskQueryDto.getUserName() + "; 业务key : " + taskQueryDto.getBusinessKey() + ",耗时："
                + (System.currentTimeMillis() - startTs));
        return retCandidateList;
    }

    /**
     * 填充待办人及用户
     *
     * @param targetTask 待填充任务对象
     * @param sourceTask 源任务对象
     */
    private void fillCandiateInfo(TaskDto targetTask, Task sourceTask) {
        List<IdentityLink> links = taskService.getIdentityLinksForTask(sourceTask.getId());
        if (links != null) {
            List<String> userIds = new ArrayList<String>();
            List<String> groupIds = new ArrayList<String>();
            for (IdentityLink i : links) {
                if (i.getType().equals("candidate")) {
                    if (StringUtils.isNotBlank(i.getUserId()))
                        userIds.add(i.getUserId());
                    if (StringUtils.isNotBlank(i.getGroupId()))
                        groupIds.add(i.getGroupId());
                }

            }
            targetTask.setCandidateUserIds(userIds);
            targetTask.setCandidateGroupIds(groupIds);
        }
    }

    @Override
    public List<TaskDto> getAssignedTasks(TaskQueryDto taskQueryDto) {
        logger.info("获取已签收任务：" + taskQueryDto.getUserName() + "; 业务key : " + taskQueryDto.getBusinessKey());
        Long startTs = System.currentTimeMillis();
        List<TaskDto> retCandidateList = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery();
        // 1. 设置签收人
        taskQuery.taskAssignee(taskQueryDto.getUserName());
        // 2. 设置BusinessKey(精确查询）
        if (StringUtils.isNotEmpty(taskQueryDto.getBusinessKey())) {
            taskQuery.processInstanceBusinessKey(taskQueryDto.getBusinessKey());
        }
        taskQuery.active();
        // 3. 按时间升序排列
        List<Task> taskList = taskQuery.orderByTaskCreateTime().asc().list();
        if (!taskList.isEmpty()) {
            // 4. 获取businessKey
            Map<String, String> procInstIdToBizKeyMap = processManagerService
                    .getProcBizKeyMapById(getProcInstIds(taskList));
            Map<String, Map<String, Object>> taskVariablesMap = this.getProcessVariables(taskList);
            retCandidateList = TaskDtoConvertor.convertTaskToDtoList(taskList, procInstIdToBizKeyMap, taskVariablesMap);
        }
        logger.info("[end]获取已签收任务：" + taskQueryDto.getUserName() + "; 业务key : " + taskQueryDto.getBusinessKey() + ",耗时："
                + (System.currentTimeMillis() - startTs));
        return retCandidateList;
    }

    /**
     * 获取运行时流程变量实例
     *
     * @param taskList 任务实例集合
     * @return <taskId, variableMap>映射关系
     */
    private Map<String, Map<String, Object>> getProcessVariables(List<Task> taskList) {
        Map<String, Map<String, Object>> taskVariablesMap = new HashMap<>();
        if (taskList != null) {
            Map<String, Object> tmpVariables;
            for (Task task : taskList) {
                tmpVariables = this.runtimeService.getVariables(task.getExecutionId());
                if (tmpVariables != null && tmpVariables.size() > 0) {
                    taskVariablesMap.put(task.getId(), tmpVariables);
                }
            }
        }
        return taskVariablesMap;
    }

    @Override
    public Long getAssignedTasksCount(TaskQueryDto taskQueryDto) {
        logger.info("获取已签收任务总数：" + taskQueryDto.getUserName());
        TaskQuery taskQuery = taskService.createTaskQuery();
        // 1. 设置签收人
        taskQuery.taskAssignee(taskQueryDto.getUserName());
        return taskQuery.count();
    }

    @Override
    public Long getCandidateTasksCount(TaskQueryDto taskQueryDto) {
        logger.info("获取未签收任务总数：" + taskQueryDto.getUserName());
        TaskQuery taskQuery = taskService.createTaskQuery();
        // 1. 设置候选人
        taskQuery.taskCandidateUser(taskQueryDto.getUserName());
        return taskQuery.count();
    }

    @Override
    public List<FormProperty> getCandidateTaskFormData(TaskQueryDto taskQueryDto) {
        logger.info("[start]获取任务表单信息，taskId : " + taskQueryDto.getTaskId());
        Long startTs = System.currentTimeMillis();
        List<FormProperty> resultFormData = null;
        TaskFormData taskFormData = formService.getTaskFormData(taskQueryDto.getTaskId());
        if (taskFormData != null) {
            resultFormData = taskFormData.getFormProperties();
        }
        logger.info("[end]获取任务表单信息，taskId : " + taskQueryDto.getTaskId() + "; 耗时："
                + (System.currentTimeMillis() - startTs));
        return resultFormData;
    }

    @Override
    public void claimTask(String taskId, String userName, Map variablesMap) {
        logger.info("[start]认领任务，taskId : " + taskId + "; userId : " + userName);
        Long startTs = System.currentTimeMillis();
        Task task = taskService.createTaskQuery().taskCandidateUser(userName).taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("认领任务不是当前用户待办或已被其他人认领！");
        }
        taskService.claim(taskId, userName);
        if (variablesMap != null && variablesMap.size() > 0) {
            taskService.setVariables(taskId, variablesMap);
        }
        logger.info("[end]认领任务，taskId : " + taskId + "; userId : " + userName + "; 耗时："
                + (System.currentTimeMillis() - startTs));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void completeTask(String taskId, Map formValues, String userId) {
        logger.info("taskId : " + taskId + "; formValues : " + formValues + "; userId : " + userId);
        Task task = taskService.createTaskQuery().taskAssignee(userId).taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("处理任务不存在，请确认后重试！");
        }
        taskService.complete(taskId, formValues);
    }

    private Set<String> getProcInstIds(List<Task> taskList) {
        Set<String> procInstIds = new HashSet<>();
        if (taskList != null) {
            for (Task task : taskList) {
                procInstIds.add(task.getProcessInstanceId());
            }
        }
        return procInstIds;
    }

    @Override
    public ExecutionDto getExecutionDtoByProcInstIdAndActId(String procInstId, String actId) {
        logger.info("[start]procInstId : " + procInstId + "; actId : " + actId);
        Long startTs = System.currentTimeMillis();
        ExecutionDto executionDto = null;
        ExecutionQuery executionQuery = this.runtimeService.createExecutionQuery();
        executionQuery.activityId(actId);
        executionQuery.processInstanceId(procInstId);
        Execution execution = executionQuery.singleResult();
        Map<String, Object> variables;
        if (execution != null) {
            executionDto = new ExecutionDto();
            executionDto.setExecutionId(execution.getId());
            variables = this.runtimeService.getVariables(execution.getId());
            if (variables != null) {
                executionDto.setVariablesMap(variables);
            }
        }
        logger.info("[end]procInstId : " + procInstId + "; actId : " + actId + "; 耗时："
                + (System.currentTimeMillis() - startTs));
        return executionDto;
    }

    @Override
    public void triggerReceiveTask(String executionId, Map formValues) {
        this.runtimeService.setVariablesLocal(executionId, formValues);
        this.runtimeService.trigger(executionId);

    }
}
