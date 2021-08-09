package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 会议费pojo
 */
@Data
public class MeetingFee extends PlanItem implements Serializable {
    /**
     * 会议内容
     */
    private String content;

    /**
     * 与课题相关性
     */
    private String relation;

    /**
     * 伙食费（人/天）
     */
    private Double meal;

    /**
     * 其他费用（人/天）
     */
    private Double otherCost;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 参会人数
     */
    private Integer amount;

    /**
     * 会议次数
     */
    private Integer meetingNum;
}