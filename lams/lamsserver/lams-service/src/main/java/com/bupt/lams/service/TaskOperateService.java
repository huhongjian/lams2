package com.bupt.lams.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OperateTypeEnum;
import com.bupt.lams.constants.ProcessTypeEnum;
import com.bupt.lams.constants.WorkflowConstant;
import com.bupt.lams.dto.TaskDto;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.dto.TaskQueryDto;
import com.bupt.lams.dto.WorkflowTaskOperateInfoDto;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.mapper.HrMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.service.process.ProcessManagerService;
import com.bupt.lams.service.task.TaskManagerService;
import com.bupt.lams.utils.UserInfoUtils;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskOperateService {
    private Logger logger = LoggerFactory.getLogger(TaskOperateService.class);

    @Resource
    AssetWorkflowService assetWorkflowService;
    @Resource
    AssetMapper assetMapper;
    @Resource
    TaskManagerService taskManagerService;
    @Resource
    TaskService taskService;
    @Resource
    HrMapper hrMapper;
    @Resource
    OperateTypeWorkflowService operateTypeWorkflowService;
    @Resource
    ProcessManagerService processManagerService;


    public void startWorkFlow(Record record, Map<String, String> startParamMap) {
        Asset asset = assetMapper.selectByPrimaryKey(record.getAid());
        // 1. 查找关联工作流definition
        String workflowKey = operateTypeWorkflowService.selectWorkflowKeyByOperateType(record.getType());
        String procInstId = processManagerService.submitStartFormDataByProcessDefinitionKey(workflowKey, asset.getId().toString(), startParamMap, asset.getCharger());
        // 2. 保存资产工作流关联关系
        AssetWorkflow assetWorkflow = new AssetWorkflow();
        assetWorkflow.setAid(asset.getId());
        assetWorkflow.setWorkflowInstId(Long.parseLong(procInstId));
        assetWorkflow.setWorkflowStartTime(new Date());
        assetWorkflowService.saveAssetWorkflow(assetWorkflow);
        TaskDto taskDto = getCandidateTskInfoByAssetIdAndUsername(record.getAid(), null);
        Map<String, String> variablesMap = new HashMap<>();
        variablesMap.put(WorkflowConstant.NEXT_USER, record.getOperator());
        taskService.setVariables(taskDto.getTaskId(), variablesMap);
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleTask(TaskHandleDto taskHandleDto) {
        Long id = taskHandleDto.getId();
        String candidateUser = taskHandleDto.getCandidateUser();
        Hr user = UserInfoUtils.getLoginedUser();
        // 查询资产工作流关联关系
        AssetWorkflow assetWorkflow = assetWorkflowService.getAssetWorkflowByAid(id);
        Asset asset = this.assetMapper.selectByPrimaryKey(id);
        if (assetWorkflow == null) {
            throw new RuntimeException("未查询到关联流程信息");
        }
        // 获取当前任务信息
        TaskDto taskDto = getAssignedTaskInfoByOrderIdAndUserName(id, user.getName());
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> variableMap = taskDto.getVariablesMap();
        String nextUser = (String) variableMap.get(WorkflowConstant.NEXT_USER);
        // 保存操作类型，用来筛选流程中不同的分支
        paramsMap.put(WorkflowConstant.OPERATE_TYPE, String.valueOf(taskHandleDto.getOperateType()));
        // 下一处理人默认一直延续，是流程发起人（申请人）
        paramsMap.put(WorkflowConstant.NEXT_USER, nextUser);
        if (taskHandleDto.getOperateType() == OperateTypeEnum.TRANSFER.getIndex()) {
            // 如果是转交的话，设置为转交人
            paramsMap.put(WorkflowConstant.NEXT_USER, candidateUser);
        }
        // 完成当前操作
        taskManagerService.completeTask(taskDto.getTaskId(), paramsMap, user.getName());
        // 如果是批准采购则更新资产状态
        if (taskHandleDto.getOperateType() == OperateTypeEnum.APPROVE.getIndex()) {
            asset.setStatus(AssetStatusEnum.APPROVE.getName());
            assetMapper.updateAssetStatusById(asset);
            return;
        }
        // 如果是入库则新增入库资产
        if (taskHandleDto.getOperateType() == OperateTypeEnum.IN.getIndex()) {
            asset.setCategory(ProcessTypeEnum.OUT.getIndex());
            asset.setStatus(AssetStatusEnum.READY.getName());
            assetMapper.insertSelective(asset);
            return;
        }
        // 如果是确认转交则更新资产状态
        if (taskHandleDto.getOperateType() == OperateTypeEnum.CONFIRM.getIndex()) {
            asset.setStatus(AssetStatusEnum.OCCUPIED.getName());
            assetMapper.updateAssetStatusById(asset);
            return;
        }
        // 如果是新资产申请被拒绝，则更新资产状态
        if (taskHandleDto.getOperateType() == OperateTypeEnum.REJECT.getIndex()) {
            asset.setStatus(AssetStatusEnum.REJECTED.getName());
            assetMapper.updateAssetStatusById(asset);
            return;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void claimAndHandleTask(TaskHandleDto taskHandleDto) {
        // 获取待办任务
        TaskDto taskDto = getCandidateTskInfoByAssetIdAndUsername(taskHandleDto.getId(), null);
        // 接受用户任务
        Hr user = UserInfoUtils.getLoginedUser();
        taskService.claim(taskDto.getTaskId(), user.getName());
        logger.info("[end]接单，工单ID:" + taskHandleDto.getId() + ";用户账号：" + user.getName());
        // 处理资产操作
        this.handleTask(taskHandleDto);
    }

    /**
     * 根据工单ID及用户账号查询已签收任务
     *
     * @param orderId  工单ID
     * @param username 用户账号
     * @return
     */
    public TaskDto getAssignedTaskInfoByOrderIdAndUserName(Long orderId, String username) {
        TaskQueryDto taskQueryDto = new TaskQueryDto();
        taskQueryDto.setBusinessKey(orderId.toString());
        taskQueryDto.setUserName(username);
        List<TaskDto> taskDtos = this.taskManagerService.getAssignedTasks(taskQueryDto);
        if (CollectionUtils.isNotEmpty(taskDtos)) {
            if (taskDtos.size() > 1) {
                throw new RuntimeException("该工单待办任务总数大于1，工单ID:" + orderId);
            }
            return taskDtos.get(0);
        } else {
            throw new RuntimeException("该工单不存在待办任务，工单ID:" + orderId);
        }
    }

    /**
     * 根据key获取受理人
     *
     * @param variableMap 流程变量Map
     * @param assigneeKey 工作流指定key
     * @return
     */
    public String getAssigneeUsername(Map<String, Object> variableMap, String assigneeKey) {
        Hr user = null;
        if (variableMap != null && variableMap.get(assigneeKey) != null) {
            try {
                user = this.hrMapper.loadUserByUsername((String) variableMap.get(assigneeKey));
            } catch (Exception e) {
                logger.info("查询用户失败: " + (String) variableMap.get(assigneeKey), e);
            }
        }
        return user != null && CollectionUtils.isNotEmpty(user.getRoles()) ? user.getName() : null;
    }

    /**
     * 根据资产ID及用户名查询未分配任务
     *
     * @param orderId  工单ID
     * @param username 用户账号
     * @return
     */
    private TaskDto getCandidateTskInfoByAssetIdAndUsername(Long orderId, String username) {
        TaskQueryDto taskQueryDto = new TaskQueryDto();
        taskQueryDto.setBusinessKey(orderId.toString());
        taskQueryDto.setUserName(username);
        List<TaskDto> taskDtos = this.taskManagerService.getCandidateTasks(taskQueryDto);
        if (CollectionUtils.isNotEmpty(taskDtos)) {
            return taskDtos.get(0);
        } else {
            throw new RuntimeException("该工单不存在未分配任务，工单ID:" + orderId);
        }
    }

    public WorkflowTaskOperateInfoDto getCandidateOrAssignedOrderWorkflowTaskOperateInfo(String username, Long orderId) {
        logger.info("[start]获取工作流操作表单信息，用户账号：" + username + "; 工单ID:" + orderId);
        // 获取待办任务
        TaskDto taskDto = this.getCandidateOrAssignedTaskInfoByOrderIdAndUsername(username, orderId);
        WorkflowTaskOperateInfoDto operateInfoDto = getTaskOperateInfoDtoByTaskDto(taskDto);
        logger.info("[end]获取工作流操作表单信息，用户账号：" + username + "; 工单ID:" + orderId);
        return operateInfoDto;
    }

    public TaskDto getCandidateOrAssignedTaskInfoByOrderIdAndUsername(String username, Long orderId) {
        TaskQueryDto taskQueryDto = new TaskQueryDto();
        taskQueryDto.setBusinessKey(orderId.toString());
        taskQueryDto.setUserName(username);
        List<TaskDto> taskDtos = this.taskManagerService.getCandidateTasks(taskQueryDto);
        if (taskDtos == null || taskDtos.size() == 0) {
            taskDtos = this.taskManagerService.getAssignedTasks(taskQueryDto);
        }
        if (CollectionUtils.isNotEmpty(taskDtos)) {
            if (taskDtos.size() > 1) {
                throw new RuntimeException("该工单签收或未签收的任务总数大于1，工单ID:" + orderId);
            }
            return taskDtos.get(0);
        } else {
            throw new RuntimeException("该工单不存在签收和未签收的任务，工单ID:" + orderId);
        }
    }

    /**
     * 根据任务DTO获取待办任务表单信息
     *
     * @param taskDto
     * @return
     */
    private WorkflowTaskOperateInfoDto getTaskOperateInfoDtoByTaskDto(TaskDto taskDto) {
        WorkflowTaskOperateInfoDto operateInfoDto = null;
        TaskQueryDto taskQueryDto = new TaskQueryDto();
        taskQueryDto.setTaskId(taskDto.getTaskId());
        // 获取待办任务表单信息
        List<FormProperty> taskForm = this.taskManagerService
                .getCandidateTaskFormData(taskQueryDto);
        if (taskForm != null) {
            operateInfoDto = new WorkflowTaskOperateInfoDto();
            String formId;
            for (FormProperty formProperty : taskForm) {
                formId = formProperty.getId();
                // operateType字段设置
                if (formId.equalsIgnoreCase(WorkflowConstant.OPERATE_TYPE)) {
                    this.initOperateTypes(operateInfoDto, formProperty);
                }
                // 转交组/人
                else if (formId.equalsIgnoreCase(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_GROUPS_USERS)) {
                    operateInfoDto.setCandidateUser(formProperty.getValue());
                }
            }
        }
        return operateInfoDto;
    }

    /**
     * 设置操作类型
     *
     * @param operateInfoDto
     * @param formProperty
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initOperateTypes(WorkflowTaskOperateInfoDto operateInfoDto, FormProperty formProperty) {
        JSONObject ops = JSONObject.parseObject(formProperty.getValue());
        List<WorkflowOperate> operateList = JSONArray.parseArray(ops.getJSONArray("ops").toJSONString(), WorkflowOperate.class);
        operateInfoDto.setOperateList(operateList);
    }
}
