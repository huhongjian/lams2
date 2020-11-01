package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.model.Order;
import com.bupt.lams.service.OrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 转交
 */
@Component
public class HandleTransfer implements IUpdateStatus {
    @Resource
    OrderService orderService;

    @Override
    public void updateStage(TaskHandleDto taskHandleDto, Order order) {
        order.setUserEmail(taskHandleDto.getCandidateUser());
        orderService.updateUserEmailById(order);
    }
}
