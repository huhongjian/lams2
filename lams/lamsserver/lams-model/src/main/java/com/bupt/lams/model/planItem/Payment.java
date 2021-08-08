package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 劳务费pojo
 */
@Data
public class Payment extends PlanItem implements Serializable {
    /**
     * 类别
     */
    private String personnelType;

    /**
     * 工作内容
     */
    private String content;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 标准
     */
    private Double standard;

    /**
     * 工作时间
     */
    private Integer hours;
}