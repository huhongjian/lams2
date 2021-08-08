package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 专家咨询费pojo
 */
@Data
public class Advisory extends PlanItem implements Serializable {
    /**
     * 人员种类
     */
    private String personnelType;

    /**
     * 工作内容
     */
    private String content;

    /**
     * 人数
     */
    private Integer amount;

    /**
     * 标准
     */
    private Double standard;

    /**
     * 天数
     */
    private Integer days;
}