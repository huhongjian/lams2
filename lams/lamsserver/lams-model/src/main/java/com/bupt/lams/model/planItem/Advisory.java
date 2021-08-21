package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
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
     * 标准（元/人/天）
     */
    private Double standard;

    /**
     * 天数
     */
    private Integer days;

    private Double similar;

    @Override
    public String toString() {
        return "Advisory{" +
                "personnelType='" + personnelType + '\'' +
                ", content='" + content + '\'' +
                ", amount=" + amount +
                ", standard=" + standard +
                ", days=" + days +
                '}';
    }

    public void computeSimilar(Advisory fee) {
        Double amountSim = Math.min(this.amount, fee.getAmount()) / (double) Math.max(this.amount, fee.getAmount());
        Double daysSim = Math.min(this.days, fee.getDays()) / (double) Math.max(this.days, fee.getDays());
        Double standardSim = Math.min(this.standard, fee.getStandard()) / Math.max(this.standard, fee.getStandard());
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = amountSim + standardSim + daysSim + textSim;
    }
}