package com.bupt.lams.service;

import com.bupt.lams.mapper.OrderWorkflowMapper;
import com.bupt.lams.model.OrderWorkflow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 工单工作流关系service
 */
@Service
public class OrderWorkflowService {
    @Resource
    OrderWorkflowMapper orderWorkflowMapper;

    public OrderWorkflow getOrderWorkflowByAid(Long aid) {
        return orderWorkflowMapper.getOrderWorkflowByAid(aid);
    }

    public void saveOrderWorkflow(OrderWorkflow orderWorkflow) {
        orderWorkflowMapper.insertSelective(orderWorkflow);
    }
}
