package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.mapper.OrderMapper;
import com.bupt.lams.model.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 归还
 */
@Component
public class HandleReturn implements IUpdateStatus {
    @Resource
    OrderMapper orderMapper;

    @Override
    public void updateStage(TaskHandleDto taskHandleDto, Order order) {
        order.setStatus(OrderStatusEnum.READY.getIndex());
        orderMapper.updateOrderStatusById(order);
        // 清空过期时间和申请理由信息
        orderMapper.resetOrderById(order.getId());
    }
}
