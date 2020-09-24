package com.bupt.lams.service;

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


    public void startWorkFlow(Record record,Map<String,String> startParamMap) {
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
    public void handleOrder(TaskHandleDto taskHandleDto, List<Integer> candidateGroups, List<String> candidateUsers) {
        Long id = taskHandleDto.getId();

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
            TaskDto taskDto = getAssignedTaskInfoByOrderIdAndUserName(id, taskHandleDto.getOperator());
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
                    paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE, taskHandleDto.getOperator());
                }
                // 三个及以上节点的工作流中第二个节点使用：WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE，中间节点不支持回复到具体人
                // 只有OpManagerWithTranferProcess在使用
                paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE, null);
                paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_LAST_PERCESS_PERSON, taskHandleDto.getOperator());
                paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_USERS, BusinessConstant.EMPTY_STR);
                if (CollectionUtils.isNotEmpty(candidateGroups)) {
                    // 设置转交组
                    paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_GROUPS,
                            StringUtils.join(candidateGroups.toArray(), BusinessConstant.COMMA_SEPARATOR));
                }
                if (CollectionUtils.isNotEmpty(candidateUsers)) {
                    // 设置转交人
                    paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_USERS,
                            StringUtils.join(candidateUsers.toArray(), BusinessConstant.COMMA_SEPARATOR));
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
            this.taskManagerService.completeTask(taskDto.getTaskId(), paramsMap, taskHandleDto.getOperator());
        } else {
            throw new RuntimeException("未查询到关联流程信息");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void claimAndHandleOrder(TaskHandleDto taskHandleDto, List<Integer> candidateGroups, List<String> candidateUsers) {
        // 1. 获取待办
        TaskDto taskDto = getCandidateTskInfoByOrderIdAndUsername(taskHandleDto.getId(),
                null);
        Asset asset = assetMapper.selectByPrimaryKey(taskHandleDto.getId());
        assetMapper.updateAssetStatusById(asset);
        // 4. 接单操作
        taskService.claim(taskDto.getTaskId(), taskHandleDto.getOperator());
        logger.info("[end]接单，工单ID:" + taskHandleDto.getId() + ";用户账号：" + taskHandleDto.getOperator());
        // 处理工单
        this.handleOrder(taskHandleDto, candidateGroups, candidateUsers);
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
     * 根据工单ID及用户名查询未分配任务
     *
     * @param orderId  工单ID
     * @param username 用户账号
     * @return
     */
    private TaskDto getCandidateTskInfoByOrderIdAndUsername(Long orderId, String username) {
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

    public WorkflowTaskOperateInfoDto getWorkflowTaskOperateInfo(String username, Long orderId) {
        logger.info("[start]获取工作流操作表单信息，用户账号：" + username + "; 工单ID:" + orderId);
        // 获取待办任务
        TaskDto taskDto = this.getAssignedTaskInfoByOrderIdAndUserName(orderId, username);
        WorkflowTaskOperateInfoDto operateInfoDto = getTaskOperateInfoDtoByTaskDto(taskDto);
        logger.info("[end]获取工作流操作表单信息，用户账号：" + username + "; 工单ID:" + orderId);
        return operateInfoDto;
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
                    this.initCandidateGroupsAndUsers(operateInfoDto, formProperty);
                }
            }
        }
        return operateInfoDto;
    }

    /**
     * 设置转交组/人
     *
     * @param operateInfoDto
     *            操作信息
     * @param formProperty
     *            表单属性信息
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initCandidateGroupsAndUsers(WorkflowTaskOperateInfoDto operateInfoDto,
                                             FormProperty formProperty) {
        String candidateGroupsAndUsersStr = formProperty.getValue();
        JSONObject jsonObject = JSONObject.parseObject(candidateGroupsAndUsersStr);
        if (jsonObject != null && jsonObject.containsKey(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_GROUPS)) {
            List<JSONObject> candidateGroupsJson = (List) jsonObject
                    .get(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_GROUPS);
            Iterator<JSONObject> iterator = candidateGroupsJson.iterator();
            Role roleDto = null;
            JSONObject tmpJsonObj = null;
            List<Role> roleList = new ArrayList<>();
            while (iterator.hasNext()) {
                tmpJsonObj = iterator.next();
                roleDto = tmpJsonObj.toJavaObject(Role.class);
                roleList.add(roleDto);
            }
            operateInfoDto.setCandidateGroups(roleList);
        }
        if (jsonObject != null && jsonObject.containsKey(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_USERS)) {
            List<JSONObject> candidateGroupsJson = (List) jsonObject
                    .get(WorkflowConstant.WORKFLOW_PARAM_KEY_CANDIDATE_USERS);
            Iterator<JSONObject> iterator = candidateGroupsJson.iterator();
            Hr adminUserDto = null;
            JSONObject tmpJsonObj = null;
            List<Hr> userList = new ArrayList<>();
            while (iterator.hasNext()) {
                tmpJsonObj = iterator.next();
                adminUserDto = tmpJsonObj.toJavaObject(Hr.class);
                userList.add(adminUserDto);
            }
            operateInfoDto.setCandidateUsers(userList);
        }
    }

    /**
     * 设置操作类型
     *
     * @param operateInfoDto
     * @param formProperty
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initOperateTypes(WorkflowTaskOperateInfoDto operateInfoDto, FormProperty formProperty) {
        Set<String> operateTypeStrs = ((HashMap) formProperty.getType().getInformation("values")).keySet();
        HashMap operateMap = (HashMap) formProperty.getType().getInformation("values");
        List<Integer> operateTypes = new ArrayList<Integer>();
        List<WorkflowOperate> operateList = new ArrayList<>();
        if (operateTypeStrs != null) {
            for (String typeStr : operateTypeStrs) {
                WorkflowOperate operate = new WorkflowOperate();
                operate.setOperateType(Integer.parseInt(typeStr));
                operate.setOperate((String) operateMap.get(typeStr));
                operateTypes.add(Integer.parseInt(typeStr));
                operateList.add(operate);
            }
        }
        operateInfoDto.setOperateTypes(operateTypes);
        operateInfoDto.setOperateList(operateList);
    }
}
