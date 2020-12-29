package com.bupt.lams.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bupt.lams.constants.*;
import com.bupt.lams.dto.TaskDto;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.dto.TaskQueryDto;
import com.bupt.lams.mapper.LamsUserMapper;
import com.bupt.lams.mapper.OrderMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.service.annotation.OperateRecord;
import com.bupt.lams.service.strategies.record.CancelRecord;
import com.bupt.lams.service.strategies.record.HandleRecord;
import com.bupt.lams.service.process.ProcessManagerService;
import com.bupt.lams.service.strategies.taskhandle.IUpdateStatus;
import com.bupt.lams.service.task.TaskManagerService;
import com.bupt.lams.utils.SpringContextUtil;
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
    OrderMapper orderMapper;
    @Resource
    OrderService orderService;
    @Resource
    TaskManagerService taskManagerService;
    @Resource
    TaskService taskService;
    @Resource
    LamsUserMapper lamsUserMapper;
    @Resource
    ProcessWorkflowService processWorkflowService;
    @Resource
    ProcessManagerService processManagerService;


    @Transactional(rollbackFor = Exception.class)
    @OperateRecord(description = "取消工单", clazz = CancelRecord.class)
    public boolean cancelOrder(TaskHandleDto taskHandleDto) {
        logger.info("[start]取消工单，工单ID:" + taskHandleDto.getId());
        Long oid = taskHandleDto.getId();
        Order order = orderService.selectBaseOrderInfoById(oid);
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
        // 清空过期时间和申请理由信息
        orderMapper.resetOrderById(order.getId());

        // 查询工单工作流关联关系
        OrderWorkflow orderWorkflow = orderWorkflowService.getOrderWorkflowByOid(oid);
        if (orderWorkflow != null) {
            this.processManagerService.hungUpProcessInstanceByBizKey(oid.toString());
        }
        logger.info("[end]取消工单，工单ID:" + taskHandleDto.getId());
        return true;
    }

    public void startWorkFlow(Order order, Integer category, Map<String, String> startParamMap) {
        Long oid = order.getId();
        // 1. 查找关联工作流definition
        String workflowKey = processWorkflowService.selectWorkflowKeyByCategory(category);
        String procInstId = processManagerService.submitStartFormDataByProcessDefinitionKey(workflowKey, order.getId().toString(), startParamMap, order.getUserEmail());
        // 2. 保存工单工作流关联关系
        OrderWorkflow orderWorkflow = new OrderWorkflow();
        orderWorkflow.setOid(order.getId());
        orderWorkflow.setWorkflowInstId(Long.parseLong(procInstId));
        orderWorkflow.setWorkflowStartTime(new Date());
        orderWorkflowService.saveOrderWorkflow(orderWorkflow);
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleTask(TaskHandleDto taskHandleDto) throws ClassNotFoundException {
        Long id = taskHandleDto.getId();
        Integer op = taskHandleDto.getOperateType();
        String candidateUser = taskHandleDto.getCandidateUser();
        LamsUser user = UserInfoUtils.getLoginedUser();
        // 查询工单工作流关联关系
        OrderWorkflow orderWorkflow = orderWorkflowService.getOrderWorkflowByOid(id);
        Order order = orderService.selectBaseOrderInfoById(id);
        if (orderWorkflow == null) {
            throw new RuntimeException("未查询到关联流程信息");
        }
        // 获取当前任务信息
        TaskDto taskDto = getAssignedTaskInfoByOrderIdAndUserName(id, user.getUsername());
        Map<String, Object> paramsMap = new HashMap<>();
        // 保存操作类型，用来筛选流程中不同的分支
        paramsMap.put(WorkflowConstant.OPERATE_TYPE, String.valueOf(op));
        // 完成当前操作
        taskManagerService.completeTask(taskDto.getTaskId(), paramsMap, user.getUsername());
        // 更新相关状态
        try {
            updateStage(taskHandleDto, order);
        } catch (ClassNotFoundException e) {
            logger.error("没找到对应的handle策略类！", e);
            throw e;
        } catch (Exception e) {
            logger.error("处理工单后，更新相关状态异常！操作类型：" + OperateTypeEnum.getNameByIndex(op), e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @OperateRecord(description = "处理工单", clazz = HandleRecord.class)
    public void claimAndHandleTask(TaskHandleDto taskHandleDto) throws ClassNotFoundException {
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
    private void updateStage(TaskHandleDto taskHandleDto, Order order) throws ClassNotFoundException {
        Integer op = taskHandleDto.getOperateType();
        String operate = TaskHandleDispatch.taskHandleMap.get(op);
        if (operate == null) {
            return;
        }
        IUpdateStatus updateStatus = (IUpdateStatus) SpringContextUtil.getBean(operate);
        updateStatus.updateStage(taskHandleDto, order);
    }
}
