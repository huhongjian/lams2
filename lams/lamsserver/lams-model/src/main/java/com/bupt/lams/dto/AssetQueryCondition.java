package com.bupt.lams.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 资产信息查询条件
 */
@Data
public class AssetQueryCondition {
    private Long aid;
    /**
     * 分页信息
     */
    private Integer page = 1;
    private Integer size = 10;
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
    private Integer status;
    private List<Integer> assetStatuses;
}
