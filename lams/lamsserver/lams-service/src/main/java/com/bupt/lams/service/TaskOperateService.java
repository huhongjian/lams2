package com.bupt.lams.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bupt.lams.constants.*;
import com.bupt.lams.dto.TaskDto;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.dto.TaskQueryDto;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.mapper.LamsUserMapper;
import com.bupt.lams.mapper.OrderAssetMapper;
import com.bupt.lams.mapper.OrderMapper;
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
import java.util.*;

@Service
public class TaskOperateService {
    private Logger logger = LoggerFactory.getLogger(TaskOperateService.class);

    @Resource
    OrderWorkflowService orderWorkflowService;
    @Resource
    OrderAssetMapper orderAssetMapper;
    @Resource
    OrderMapper orderMapper;
    @Resource
    AssetMapper assetMapper;
    @Resource
    TaskManagerService taskManagerService;
    @Resource
    TaskService taskService;
    @Resource
    LamsUserMapper lamsUserMapper;
    @Resource
    OperateTypeWorkflowService operateTypeWorkflowService;
    @Resource
    ProcessManagerService processManagerService;


    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(TaskHandleDto taskHandleDto) {
        logger.info("[start]取消工单，工单ID:" + taskHandleDto.getId());
        Long oid = taskHandleDto.getId();
        Order order = orderMapper.selectByPrimaryKey(oid);
        LamsUser user = UserInfoUtils.getLoginedUser();
        if (!order.getUserEmail().equals(user.getUsername())) {
            throw new RuntimeException("仅创建人可发起撤销操作！");
        }
        if (order.getStatus() == OrderStatusEnum.CLOSED.getIndex()) {
            return true;
        }
        order.setStatus(OrderStatusEnum.CLOSED.getIndex());

        // 设置工单状态
        orderMapper.updateOrderStatusById(order);

        // 3. 查询工单工作流关联关系
        OrderWorkflow orderWorkflow = orderWorkflowService.getOrderWorkflowByOid(oid);
        if (orderWorkflow != null) {
            this.processManagerService.hungUpProcessInstanceByBizKey(oid.toString());
        }
        logger.info("[end]取消工单，工单ID:" + taskHandleDto.getId());
        return true;
    }

    public void startWorkFlow(Record record, Map<String, String> startParamMap) {
        Order order = orderMapper.selectByPrimaryKey(record.getOid());
        // 1. 查找关联工作流definition
        String workflowKey = operateTypeWorkflowService.selectWorkflowKeyByOperateType(record.getType());
        String procInstId = processManagerService.submitStartFormDataByProcessDefinitionKey(workflowKey, order.getId().toString(), startParamMap, order.getUserEmail());
        // 2. 保存工单工作流关联关系
        OrderWorkflow orderWorkflow = new OrderWorkflow();
        orderWorkflow.setOid(order.getId());
        orderWorkflow.setWorkflowInstId(Long.parseLong(procInstId));
        orderWorkflow.setWorkflowStartTime(new Date());
        orderWorkflowService.saveOrderWorkflow(orderWorkflow);
        TaskDto taskDto = getCandidateTskInfoByOrderIdAndUsername(record.getOid(), null);
        Map<String, String> variablesMap = new HashMap<>();
        variablesMap.put(WorkflowConstant.NEXT_USER, record.getOperatorMail());
        taskService.setVariables(taskDto.getTaskId(), variablesMap);
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleTask(TaskHandleDto taskHandleDto) {
        Long id = taskHandleDto.getId();
        String candidateUser = taskHandleDto.getCandidateUser();
        LamsUser user = UserInfoUtils.getLoginedUser();
        // 查询工单工作流关联关系
        OrderWorkflow orderWorkflow = orderWorkflowService.getOrderWorkflowByOid(id);
        Order order = this.orderMapper.selectByPrimaryKey(id);
        if (orderWorkflow == null) {
            throw new RuntimeException("未查询到关联流程信息");
        }
        // 获取当前任务信息
        TaskDto taskDto = getAssignedTaskInfoByOrderIdAndUserName(id, user.getUsername());
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
        taskManagerService.completeTask(taskDto.getTaskId(), paramsMap, user.getUsername());
        // 更新相关状态
        updateStage(taskHandleDto, order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void claimAndHandleTask(TaskHandleDto taskHandleDto) {
        // 获取待办任务
        TaskDto taskDto = getCandidateTskInfoByOrderIdAndUsername(taskHandleDto.getId(), null);
        // 接受用户任务
        LamsUser user = UserInfoUtils.getLoginedUser();
        taskService.claim(taskDto.getTaskId(), user.getUsername());
        logger.info("[end]接单，工单ID:" + taskHandleDto.getId() + ";用户账号：" + user.getUsername());
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
        LamsUser user = null;
        if (variableMap != null && variableMap.get(assigneeKey) != null) {
            try {
                user = this.lamsUserMapper.loadUserByUsername((String) variableMap.get(assigneeKey));
            } catch (Exception e) {
                logger.info("查询用户失败: " + variableMap.get(assigneeKey), e);
            }
        }
        return user != null && CollectionUtils.isNotEmpty(user.getRoles()) ? user.getUsername() : null;
    }

    /**
     * 根据资产ID及用户名查询未分配任务
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

    public List<Operate> getCandidateOrAssignedOrderWorkflowTaskOperateInfo(String username, Long orderId) {
        logger.info("[start]获取工作流操作表单信息，用户账号：" + username + "; 工单ID:" + orderId);
        // 获取待办任务
        TaskDto taskDto = this.getCandidateOrAssignedTaskInfoByOrderIdAndUsername(username, orderId);
        List<Operate> operateList = getTaskOperateInfoDtoByTaskDto(taskDto);
        logger.info("[end]获取工作流操作表单信息，用户账号：" + username + "; 工单ID:" + orderId);
        return operateList;
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
    private List<Operate> getTaskOperateInfoDtoByTaskDto(TaskDto taskDto) {
        List<Operate> operateList = new ArrayList<>();
        TaskQueryDto taskQueryDto = new TaskQueryDto();
        taskQueryDto.setTaskId(taskDto.getTaskId());
        // 获取待办任务表单信息
        List<FormProperty> taskForm = this.taskManagerService.getCandidateTaskFormData(taskQueryDto);
        if (taskForm != null) {
            for (FormProperty formProperty : taskForm) {
                String formId = formProperty.getId();
                // operateType字段设置
                if (formId.equalsIgnoreCase(WorkflowConstant.OPERATE_TYPE)) {
                    operateList = initOperateTypes(formProperty);
                }
            }
        }
        return operateList;
    }

    /**
     * 设置操作类型
     *
     * @param formProperty
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Operate> initOperateTypes(FormProperty formProperty) {
        JSONObject ops = JSONObject.parseObject(formProperty.getValue());
        return JSONArray.parseArray(ops.getJSONArray("ops").toJSONString(), Operate.class);
    }

    public List<Long> getAssignedOrderIds(String userName) {
        return getOrderIdsFromTasks(getAssignedTasks(userName));
    }

    public List<TaskDto> getAssignedTasks(String userName) {
        logger.info("获取用户已签收任务，用户账号" + userName);
        TaskQueryDto taskQueryDto = new TaskQueryDto();
        taskQueryDto.setUserName(userName);
        return this.taskManagerService.getAssignedTasks(taskQueryDto);
    }

    public List<Long> getCandidateOrderIds(String userName) {
        return getOrderIdsFromTasks(getCandidateTasks(userName));
    }

    public List<TaskDto> getCandidateTasks(String userName) {
        logger.info("获取用户未签收任务，用户账号" + userName);
        TaskQueryDto taskQueryDto = new TaskQueryDto();
        taskQueryDto.setUserName(userName);
        return this.taskManagerService.getCandidateTasks(taskQueryDto);
    }

    private List<Long> getOrderIdsFromTasks(List<TaskDto> tasks) {
        List<Long> result = new ArrayList<Long>();
        if (tasks != null) {
            for (TaskDto task : tasks) {
                result.add(Long.valueOf(task.getBusinessKey()));
            }
        }
        return result;
    }

    /**
     * 完成工作流任务后，更新相关状态
     *
     * @param taskHandleDto
     * @param order
     */
    private void updateStage(TaskHandleDto taskHandleDto, Order order) {
        Long id = taskHandleDto.getId();
        // 如果是批准采购则更新工单状态
        if (taskHandleDto.getOperateType() == OperateTypeEnum.APPROVE.getIndex()) {
            order.setStatus(OrderStatusEnum.APPROVE.getIndex());
            // 更新工单状态
            orderMapper.updateOrderStatusById(order);
            return;
        }
        // 如果是入库则新增入库资产
        if (taskHandleDto.getOperateType() == OperateTypeEnum.IN.getIndex()) {
            order.setCategory(ProcessTypeEnum.OUT.getIndex());
            order.setStatus(OrderStatusEnum.READY.getIndex());
            order.setReason(null);
            orderMapper.insertSelective(order);
            OrderAsset orderAsset = new OrderAsset();
            Long aid = orderAssetMapper.getAidByOid(id);
            orderAsset.setAid(aid);
            orderAsset.setOid(order.getId());
            orderAsset.setCreateTime(new Date());
            orderAsset.setUpdateTime(new Date());
            orderAssetMapper.insertSelective(orderAsset);
            // 更新资产入库时间
            Asset asset = new Asset();
            asset.setId(aid);
            asset.setStatus(AssetStatusEnum.NORMAL.getIndex());
            asset.setReadyDate(new Date());
            assetMapper.updateAsset(asset);
            return;
        }
        // 如果是新资产申请被拒绝，则更新工单状态
        if (taskHandleDto.getOperateType() == OperateTypeEnum.REJECT.getIndex()) {
            order.setStatus(OrderStatusEnum.REJECTED.getIndex());
            orderMapper.updateOrderStatusById(order);
            Asset asset = order.getAsset();
            asset.setStatus(AssetStatusEnum.REJECTED.getIndex());
            assetMapper.updateAssetStatus(asset);
            return;
        }
        // 如果是确认转交则更新工单状态
        if (taskHandleDto.getOperateType() == OperateTypeEnum.CONFIRM.getIndex()) {
            order.setStatus(OrderStatusEnum.OCCUPIED.getIndex());
            orderMapper.updateOrderStatusById(order);
            return;
        }
        // 如果是归还则更新工单状态
        if (taskHandleDto.getOperateType() == OperateTypeEnum.RETURN.getIndex()) {
            order.setStatus(OrderStatusEnum.READY.getIndex());
            orderMapper.updateOrderStatusById(order);
            // 清空过期时间和申请理由信息
            orderMapper.resetOrderById(order.getId());
            return;
        }
        // 如果是拒绝借用则更新工单状态
        if (taskHandleDto.getOperateType() == OperateTypeEnum.REFUSE.getIndex()) {
            order.setStatus(OrderStatusEnum.REFUSED.getIndex());
            orderMapper.updateOrderStatusById(order);
            // 清空过期时间和申请理由信息
            orderMapper.resetOrderById(order.getId());
            return;
        }
    }
}
