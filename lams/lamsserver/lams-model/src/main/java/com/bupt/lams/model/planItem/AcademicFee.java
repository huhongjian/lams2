package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 国内学术会议差旅费pojo
 */
@Data
public class AcademicFee extends PlanItem implements Serializable {
    /**
     * 会议名称
     */
    private String meetingName;

    /**
     * 人员类别
     */
    private String personnelType;

    /**
     * 人次
     */
    private Integer times;

    /**
     * 年度
     */
    private Integer year;

    /**
     * 会期（天）
     */
    private Integer days;

    /**
     * 往返交通费（元）
     */
    private Double transFee;

    /**
     * 住宿及补助费（元）
     */
    private Double accommodationFee;
}