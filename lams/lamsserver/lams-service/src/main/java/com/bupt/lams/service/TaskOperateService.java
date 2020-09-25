package com.bupt.lams.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.BusinessConstant;
import com.bupt.lams.constants.OperateTypeEnum;
import com.bupt.lams.constants.WorkflowConstant;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.dto.WorkflowTaskOperateInfoDto;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.mapper.HrMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.dto.TaskDto;
import com.bupt.lams.dto.TaskQueryDto;
import com.bupt.lams.service.process.ProcessManagerService;
import com.bupt.lams.service.task.TaskManagerService;
import com.bupt.lams.utils.UserInfoUtils;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
        String procInstId = processManagerService.submitStartFormDataByProcessDefinitionKey(workflowKey, asset.getId().toString(), startParamMap, asset.getApplicant());
        // 2. 保存资产工作流关联关系
        AssetWorkflow assetWorkflow = new AssetWorkflow();
        assetWorkflow.setAid(asset.getId());
        assetWorkflow.setWorkflowInstId(Long.parseLong(procInstId));
        assetWorkflow.setWorkflowStartTime(new Date());
        assetWorkflowService.saveAssetWorkflow(assetWorkflow);
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleOrder(TaskHandleDto taskHandleDto, String candidateUser) {
        Long id = taskHandleDto.getId();
        Hr user = UserInfoUtils.getLoginedUser();

        // 2. 查询工单工作流关联关系
        AssetWorkflow assetWorkflow = assetWorkflowService.getAssetWorkflowByAid(id);

        Asset asset = this.assetMapper.selectByPrimaryKey(id);
        // 3. 如果是解释类型则更新工单状态
        if (taskHandleDto.getOperateType() == OperateTypeEnum.REJECT.getIndex()) {
            asset.setStatus(AssetStatusEnum.REJECTED.getName());
        }
//        this.orderService.updateOrderStatusById(order);

        // 4. 转交/回复工单
        if (assetWorkflow != null) {
            TaskDto taskDto = getAssignedTaskInfoByOrderIdAndUserName(id, user.getName());
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_OPERATE_TYPE,
                    String.valueOf(taskHandleDto.getOperateType()));
            // 获取受理人1
            Map<String, Object> variableMap = taskDto.getVariablesMap();
            // 多个节点的工作流中第一个节点使用：WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE
            String firstAssignee = this.getAssigneeUsername(variableMap,
                    WorkflowConstant.WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE);
            String responseToAssignee = this.getAssigneeUsername(variableMap,
                    WorkflowConstant.WORKFLOW_PARAM_KEY_LAST_PERCESS_PERSON);
            // 当前任务的上个节点转交人，回复时使用
            HashMap<String, String> taskNameMapReplyUser = variableMap
                    .get(WorkflowConstant.WORKFLOW_PARAM_KEY_TASKNAME_MAP_REPLYUSER) != null
                    ? (HashMap<String, String>) variableMap
                    .get(WorkflowConstant.WORKFLOW_PARAM_KEY_TASKNAME_MAP_REPLYUSER)
                    : new HashMap<String, String>();
            // 更新当前任务的上个节点的转交人，上一步为回复时，不进行更新
            if (responseToAssignee != null && !responseToAssignee.equals("")) {
                taskNameMapReplyUser.put(taskDto.getName(), responseToAssignee);
            }
            // TODO 采用WORKFLOW_PARAM_KEY_TASKNAME_MAP_REPLYUSER来替换WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE、WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE，支持多个节点场景下回复给上游处理节点
            paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_TASKNAME_MAP_REPLYUSER, taskNameMapReplyUser);
            if (taskHandleDto.getOperateType() == OperateTypeEnum.TRANSFER.getIndex()) {
                if (firstAssignee != null && !firstAssignee.equals("")) {
                    // 设置受理人1信息
                    paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE, user.getName());
                }
                // 三个及以上节点的工作流中第二个节点使用：WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE，中间节点不支持回复到具体人
                // 只有OpManagerWithTranferProcess在使用
                paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE, null);
                paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_LAST_PERCESS_PERSON, user.getName());
                paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_USERS, BusinessConstant.EMPTY_STR);
                if (StringUtils.isNotEmpty(candidateUser)) {
                    // 设置转交人
                    paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_USERS, StringUtils.join(candidateUser, BusinessConstant.COMMA_SEPARATOR));
                }
            }
            // 设置下一审批人为第一受理人
            else if (taskHandleDto.getOperateType() == OperateTypeEnum.TRANSFER.getIndex()) {
                paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_LAST_PERCESS_PERSON, null);
                paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE,
                        taskNameMapReplyUser.get(taskDto.getName()));
                // 三个及以上节点的工作流中第二个节点使用：WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE，中间节点不支持回复到具体人
                // 只有OpManagerWithTranferProcess在使用
                paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE,
                        taskNameMapReplyUser.get(taskDto.getName()));
            }
            this.taskManagerService.completeTask(taskDto.getTaskId(), paramsMap, user.getName());
        } else {
            throw new RuntimeException("未查询到关联流程信息");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void claimAndHandleOrder(TaskHandleDto taskHandleDto, String candidateUser) {
        // 1. 获取待办
        TaskDto taskDto = getCandidateTskInfoByAssetIdAndUsername(taskHandleDto.getId(), null);
        Asset asset = assetMapper.selectByPrimaryKey(taskHandleDto.getId());
        assetMapper.updateAssetStatusById(asset);
        // 4. 接单操作
        String operate = OperateTypeEnum.getNameByIndex(taskHandleDto.getOperateType());
        taskService.claim(taskDto.getTaskId(), operate);
        logger.info("[end]接单，工单ID:" + taskHandleDto.getId() + ";用户账号：" + operate);
        // 处理工单
        this.handleOrder(taskHandleDto, candidateUser);
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
                if (formId.equalsIgnoreCase(WorkflowConstant.WORKFLOW_PARAM_KEY_OPERATE_TYPE)) {
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
