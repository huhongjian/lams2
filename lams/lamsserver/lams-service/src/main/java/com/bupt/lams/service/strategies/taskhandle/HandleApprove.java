package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.mapper.OrderMapper;
import com.bupt.lams.model.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 批准采购
 */
@Component
public class HandleApprove implements IUpdateStatus {
    @Resource
    OrderMapper orderMapper;

    @Override
    public void updateStage(TaskHandleDto taskHandleDto, Order order) {
        order.setStatus(OrderStatusEnum.APPROVE.getIndex());
        // 更新工单状态
        orderMapper.updateOrderStatusById(order);
    }
}
