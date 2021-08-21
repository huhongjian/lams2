package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
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
     * 注册费（元/人）
     */
    private Double registrationFee;

    /**
     * 往返交通费（元/人/天）
     */
    private Double transFee;

    /**
     * 住宿及补助费（元/人/天）
     */
    private Double accommodationFee;

    private Double similar;

    @Override
    public String toString() {
        return "AcademicFee{" +
                "meetingName='" + meetingName + '\'' +
                ", personnelType='" + personnelType + '\'' +
                ", times=" + times +
                ", year=" + year +
                ", days=" + days +
                ", registrationFee=" + registrationFee +
                ", transFee=" + transFee +
                ", accommodationFee=" + accommodationFee +
                '}';
    }

    public void computeSimilar(AcademicFee fee) {
        Double timesSim = Math.min(this.times, fee.getTimes()) / (double) Math.max(this.times, fee.getTimes());
        Double yearSim = Math.min(this.year, fee.getYear()) / (double) Math.max(this.year, fee.getYear());
        Double daysSim = Math.min(this.days, fee.getDays()) / (double) Math.max(this.days, fee.getDays());
        Double registrationFeeSim = Math.min(this.registrationFee, fee.getRegistrationFee()) / Math.max(this.registrationFee, fee.getRegistrationFee());
        Double transFeeSim = Math.min(this.transFee, fee.getTransFee()) / Math.max(this.transFee, fee.getTransFee());
        Double accommodationFeeSim = Math.min(this.accommodationFee, fee.getAccommodationFee()) / Math.max(this.accommodationFee, fee.getAccommodationFee());
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = timesSim + yearSim + daysSim + registrationFeeSim + transFeeSim + accommodationFeeSim + textSim;
    }
}