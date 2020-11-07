package com.bupt.lams.mapper;

import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.model.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    /**
     * 根据条件获取工单
     *
     * @param condition
     * @return
     */
    List<Order> getOrderByCondition(OrderQueryCondition condition);

    /**
     * 获取符合条件的工单总数
     *
     * @param condition
     * @return
     */
    Long getTotalByCondition(OrderQueryCondition condition);

    List<Order> getStuOutByCondition(OrderQueryCondition condition);

    Long getStuOutTotalByCondition(OrderQueryCondition condition);

    Order selectBaseOrderInfoById(Long id);

    void updateOrderStatusById(Order order);

    void updateUserEmailById(Order order);

    void updateOrder(Order order);

    void resetOrderById(@Param("oid") Long oid);

    int insertSelective(Order order);

    void deleteManyByOids(List<Long> oids);
}