package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.model.Order;

public interface IUpdateStatus {
    void updateStage(TaskHandleDto taskHandleDto, Order order);
}
