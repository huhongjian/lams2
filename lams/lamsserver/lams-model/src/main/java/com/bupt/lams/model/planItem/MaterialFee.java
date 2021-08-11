package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 材料费信息pojo
 */
@Data
public class MaterialFee extends PlanItem implements Serializable {
    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 规格
     */
    private String specification;

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
     * 单位
     */
    private String unit;
}