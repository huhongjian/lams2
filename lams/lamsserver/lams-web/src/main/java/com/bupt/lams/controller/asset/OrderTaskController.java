package com.bupt.lams.controller.asset;

import com.bupt.lams.constants.OperateTypeEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.dto.WorkflowTaskOperateInfoDto;
import com.bupt.lams.model.*;
import com.bupt.lams.service.OrderService;
import com.bupt.lams.service.TaskOperateService;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 资产状态流转相关
 */
@RestController
@RequestMapping("/order/task")
public class OrderTaskController {

    @Resource
    TaskOperateService taskOperateService;
    @Resource
    OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(OrderTaskController.class);

    /**
     * 取消工单
     *
     * @param taskHandleDto
     * @return
     */
    @RequestMapping("cancel")
    public RespBean cancelOrder(@RequestBody TaskHandleDto taskHandleDto) {
        RespBean response = new RespBean();
        if (taskHandleDto != null) {
            Integer operateType = taskHandleDto.getOperateType();
            // 验证操作类型
            if (operateType == null || !operateType.equals(OperateTypeEnum.CANCEL.getIndex())) {
                logger.warn("/order/task/cancel,取消工单操作类型错误，应为取消，实际传入的operateType=" + operateType);
                response.setStatus(500);
                response.setMsg("操作类型错误，请重试");
                return response;
            }
            try {
                // 取消工单
                taskOperateService.cancelOrder(taskHandleDto);
                response.setStatus(200);
            } catch (Exception e) {
                logger.error("取消工单异常：", e);
                response.setStatus(500);
                response.setMsg("取消工单异常，请重试");
            }
        }
        return response;
    }

    @RequestMapping("handleTask")
    @ResponseBody
    public RespBean handleTask(@RequestBody TaskHandleDto taskHandleDto) {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            taskOperateService.claimAndHandleTask(taskHandleDto);
        } catch (Exception e) {
            logger.error("当前流程操作失败！", e);
            response.setStatus(500);
            response.setMsg("资产操作异常，请稍后重试");
        }
        return response;
    }

    @RequestMapping("getCandidateTaskBranchInfo")
    @ResponseBody
    public RespBean getCandidateTaskBranchInfo(@RequestParam Long id) {
        RespBean response = new RespBean();
        WorkflowTaskOperateInfoDto opInfoDto;
        LamsUser user = UserInfoUtils.getLoginedUser();
        // 1. 获取资产信息
        Order order = orderService.selectByPrimaryKey(id);
        if (order == null) {
            response.setStatus(500);
            response.setMsg("指定工单不存在！");
            return response;
        }
        try {
            opInfoDto = taskOperateService.getCandidateOrAssignedOrderWorkflowTaskOperateInfo(user.getUsername(), id);
        } catch (Exception e) {
            opInfoDto = new WorkflowTaskOperateInfoDto();
        }
        // 如果当前用户是工单创建人并且工单不是终止状态
        if (order.getUserEmail().equals(user.getUsername()) && !order.getStatus().equals(OrderStatusEnum.READY.getName())) {
            WorkflowOperate cancel = new WorkflowOperate();
            cancel.setOperateType(OperateTypeEnum.CANCEL.getIndex());
            cancel.setOperate(OperateTypeEnum.CANCEL.getName());
            opInfoDto.getOperateList().add(cancel);
        }
        response.setObj(opInfoDto);
        response.setStatus(200);
        return response;
    }

    @RequestMapping("getMyApply")
    @ResponseBody
    public RespPageBean getMyApply(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Order order, Date[] beginDateScope) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        order.setUserEmail(user.getUsername());
        return orderService.getOrderByPage(page, size, order, beginDateScope);
    }

    @RequestMapping("getMyTask")
    @ResponseBody
    public RespPageBean getMyTask(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Order order, Date[] beginDateScope) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        List<Long> assignedOrderIds = taskOperateService.getAssignedOrderIds(user.getUsername());
        List<Long> candidateOrderIds = taskOperateService.getCandidateOrderIds(user.getUsername());
        Set<Long> oids = new HashSet<>();
        for (Long id : assignedOrderIds) {
            oids.add(id);
        }
        for (Long id : candidateOrderIds) {
            oids.add(id);
        }
        OrderQueryCondition condition = new OrderQueryCondition(order, page, size, oids, beginDateScope);
        return orderService.getOrderByCondition(condition);
    }
}
