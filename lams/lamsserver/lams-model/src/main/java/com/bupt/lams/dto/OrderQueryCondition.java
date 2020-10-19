package com.bupt.lams.dto;

import com.bupt.lams.model.Asset;
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
    private Long oid;
    /**
     * 工单相关信息
     */
    private Integer category;
    private Integer status;
    private String userEmail;
    /**
     * 分页信息
     */
    private Integer page = 1;
    private Integer size = 10;
    /**
     * ID列表，用于批量查询
     */
    private Set<Long> ids;
    /**
     * 时间范围
     */
    private Date startDate;
    private Date endDate;
    /**
     * 价格范围
     */
    private Double priceLow;
    private Double priceHigh;
    /**
     * 资产信息
     */
    private String type;
    private String brand;
    private List<Integer> assetStatuses;

    public void setOrderInfo(Order order) {
        if (order == null) {
            return;
        }
        this.category = order.getCategory();
        this.status = order.getStatus();
        this.userEmail = order.getUserEmail();
    }

    public void setAssetInfo(Asset asset) {
        if (asset == null) {
            return;
        }
        this.type = asset.getType();
        this.brand = asset.getBrand();
    }
}
