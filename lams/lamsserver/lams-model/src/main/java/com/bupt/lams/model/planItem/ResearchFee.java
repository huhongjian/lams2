package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
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
     * 往返交通费（元/人/天）
     */
    private Double transFee;

    /**
     * 住宿（元/人/天）
     */
    private Double accommodationFee;

    /**
     * 公杂费（元/人/天）
     */
    private Double otherFee;

    /**
     * 天数
     */
    private Integer days;

    private Double similar;

    @Override
    public String toString() {
        return "ResearchFee{" +
                "place='" + place + '\'' +
                ", purpose='" + purpose + '\'' +
                ", personnelType='" + personnelType + '\'' +
                ", times=" + times +
                ", transFee=" + transFee +
                ", accommodationFee=" + accommodationFee +
                ", otherFee=" + otherFee +
                ", days=" + days +
                '}';
    }

    public void computeSimilar(ResearchFee fee) {
        Double timesSim = Math.min(this.times, fee.getTimes()) / (double) Math.max(this.times, fee.getTimes());
        Double daysSim = Math.min(this.days, fee.getDays()) / (double) Math.max(this.days, fee.getDays());
        Double otherFeeSim = Math.min(this.otherFee, fee.getOtherFee()) / Math.max(this.otherFee, fee.getOtherFee());
        Double transFeeSim = Math.min(this.transFee, fee.getTransFee()) / Math.max(this.transFee, fee.getTransFee());
        Double accommodationFeeSim = Math.min(this.accommodationFee, fee.getAccommodationFee()) / Math.max(this.accommodationFee, fee.getAccommodationFee());
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = timesSim + otherFeeSim + daysSim + transFeeSim + accommodationFeeSim + textSim;
    }
}