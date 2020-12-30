package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.model.Order;
import com.bupt.lams.service.OrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 归还资产审核未通过
 */
@Component
public class HandleTurnDown implements IUpdateStatus {
    @Resource
    OrderService orderService;

    @Override
    public void updateStage(TaskHandleDto taskHandleDto, Order order) {
        order.setStatus(OrderStatusEnum.TURN_DOWN.getIndex());
        orderService.updateOrderStatusById(order);
    }
}
