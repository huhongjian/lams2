package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
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
     * 伙食费（元/人/天）
     */
    private Double meal;

    /**
     * 其他费用（元/人/天）
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

    private Double similar;

    @Override
    public String toString() {
        return "MeetingFee{" +
                "content='" + content + '\'' +
                ", relation='" + relation + '\'' +
                ", meal=" + meal +
                ", otherCost=" + otherCost +
                ", days=" + days +
                ", amount=" + amount +
                ", meetingNum=" + meetingNum +
                '}';
    }

    public void computeSimilar(MeetingFee fee) {
        Double amountSim = Math.min(this.amount, fee.getAmount()) / (double) Math.max(this.amount, fee.getAmount());
        Double meetingNumSim = Math.min(this.meetingNum, fee.getMeetingNum()) / (double) Math.max(this.meetingNum, fee.getMeetingNum());
        Double daysSim = Math.min(this.days, fee.getDays()) / (double) Math.max(this.days, fee.getDays());
        Double otherCostFeeSim = Math.min(this.otherCost, fee.getOtherCost()) / Math.max(this.otherCost, fee.getOtherCost());
        Double mealSim = Math.min(this.meal, fee.getMeal()) / Math.max(this.meal, fee.getMeal());
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = amountSim + meetingNumSim + daysSim + otherCostFeeSim + mealSim + textSim;
    }
}