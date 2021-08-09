package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 课题调研差旅费pojo
 */
@Data
public class ResearchFee extends PlanItem implements Serializable {
    /**
     * 调研地点
     */
    private String place;

    /**
     * 调研目的
     */
    private String purpose;

    /**
     * 人员类别
     */
    private String personnelType;

    /**
     * 人次
     */
    private Integer times;

    /**
     * 往返交通费（元）
     */
    private Double transFee;

    /**
     * 住宿（元）
     */
    private Double accommodationFee;

    /**
     * 公杂费（元）
     */
    private Double otherFee;

    /**
     * 天数
     */
    private Integer days;
}