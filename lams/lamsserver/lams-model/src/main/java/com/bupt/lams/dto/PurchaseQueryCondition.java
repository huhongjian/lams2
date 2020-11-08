package com.bupt.lams.dto;

import lombok.Data;

import java.util.Date;

/**
 * 订单信息查询条件
 */
@Data
public class PurchaseQueryCondition {
    private Long poid;
    private String name;
    private String creatorEmail;
    /**
     * 分页信息
     */
    private Integer page = 1;
    private Integer size = 10;

    /**
     * 购买时间范围
     */
    private Date startDateForPurchase;
    private Date endDateForPurchase;

    /**
     * 是否有发票
     */
    private Boolean hasInvoice;

    /**
     * 发票时间范围
     */
    private Date startDateForInvoice;
    private Date endDateForInvoice;

    /**
     * 订单总价范围
     */
    private Double totalLow;
    private Double totalHigh;

    /**
     * 实际支付范围
     */
    private Double payLow;
    private Double payHigh;
}
