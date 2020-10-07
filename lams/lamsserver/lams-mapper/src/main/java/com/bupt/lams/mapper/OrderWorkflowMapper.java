package com.bupt.lams.mapper;

import com.bupt.lams.model.OrderWorkflow;

public interface OrderWorkflowMapper {
    int insertSelective(OrderWorkflow orderWorkflow);

    /**
     * 根据工单id获取工作流实例id
     * <p>
     * 一个工单可能会开启多个流程实例
     * 例如：归还之后重新借用
     * 这里只返回最新的流程实例
     *
     * @param aid
     * @return
     */
    OrderWorkflow getOrderWorkflowByOid(Long aid);
}