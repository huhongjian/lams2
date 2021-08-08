package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 测试化验加工费pojo
 */
@Data
public class TestingFee extends PlanItem implements Serializable {
    /**
     * 加工内容
     */
    private String content;

    /**
     * 次数
     */
    private Integer times;

    /**
     * 单价（万元）
     */
    private Double unitPrice;
}