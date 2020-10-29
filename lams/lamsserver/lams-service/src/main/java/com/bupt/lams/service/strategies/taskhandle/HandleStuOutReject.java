package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.mapper.LamsUserMapper;
import com.bupt.lams.mapper.OrderMapper;
import com.bupt.lams.model.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 离退申请拒绝
 */
@Component
public class HandleStuOutReject implements IUpdateStatus {
    @Resource
    OrderMapper orderMapper;

    @Override
    public void updateStage(TaskHandleDto taskHandleDto, Order order) {
        order.setStatus(OrderStatusEnum.REJECTED.getIndex());
        orderMapper.updateOrderStatusById(order);
    }
}
