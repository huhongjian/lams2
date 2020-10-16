package com.bupt.lams.mapper;

import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.model.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderMapper {
    List<Order> getOrderByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("order") Order order, @Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("order") Order order, @Param("beginDateScope") Date[] beginDateScope);

    Order selectByPrimaryKey(Long id);

    void updateOrderStatusById(Order order);

    void updateOrder(Order order);

    int insertSelective(Order order);

    void deleteManyByOids(List<Integer> oids);

    List<Order> getOrderByCondition(OrderQueryCondition condition);
}