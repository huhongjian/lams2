package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 购买订单类
 */
@Data
public class PurchaseOrder {
    private Long id;
    /**
     * 订单名称
     */
    private String name;
    /**
     * 订单总价
     */
    private Double total;
    /**
     * 订单优惠
     */
    private Double discount;
    /**
     * 实际支付
     */
    private Double pay;
    /**
     * 购买时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date purchaseDate;
    /**
     * 是否有发票
     */
    private Boolean hasInvoice;
    /**
     * 发票时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date invoiceDate;
    /**
     * 订单备注
     */
    private String remark;
    private String creatorEmail;
    private String updaterEmail;
    private LamsUser creator;
    private LamsUser updater;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date updateTime;
    /**
     * 订单包含的资产信息
     */
    List<Asset> assetList;
    private List<PurchasePic> fileList;
}
