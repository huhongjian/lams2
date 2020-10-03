package com.bupt.lams.dto;

import com.bupt.lams.model.Order;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 工单查询条件
 */
@Data
public class OrderQueryCondition {
    /**
     * 工单信息
     */
    private Order order;
    /**
     * 分页信息
     */
    private Integer page;
    private Integer size;
    /**
     * ID列表，用于批量查询
     */
    private Set<Long> ids;
    /**
     * 时间范围
     */
    private Date[] beginDateScope;

    public OrderQueryCondition() {
        super();
    }

    public OrderQueryCondition(Order order, Integer page, Integer size, Set<Long> ids, Date[] beginDateScope) {
        this.order = order;
        this.page = page;
        this.size = size;
        this.ids = ids;
        this.beginDateScope = beginDateScope;
    }
}
