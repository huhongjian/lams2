package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 其他支出pojo
 */
@Data
public class OtherFee extends PlanItem implements Serializable {
    /**
     * 类别
     */
    private String type;
}