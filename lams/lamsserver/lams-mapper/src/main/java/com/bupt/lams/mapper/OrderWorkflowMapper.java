package com.bupt.lams.mapper;

import com.bupt.lams.model.OrderWorkflow;

public interface OrderWorkflowMapper {
    int insertSelective(OrderWorkflow orderWorkflow);
    OrderWorkflow getOrderWorkflowByAid(Long aid);
}