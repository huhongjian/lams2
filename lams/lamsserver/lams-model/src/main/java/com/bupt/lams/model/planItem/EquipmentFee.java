package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 设备费信息pojo
 */
@Data
public class EquipmentFee extends PlanItem implements Serializable {
    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 用途
     */
    private String usage;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 单价
     */
    private Double unitPrice;

    /**
     * 合计
     */
    private Double subTotal;
}