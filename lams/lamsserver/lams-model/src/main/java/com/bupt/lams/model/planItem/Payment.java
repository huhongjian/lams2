package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
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

    private Double similar;

    @Override
    public String toString() {
        return "Payment{" +
                "personnelType='" + personnelType + '\'' +
                ", content='" + content + '\'' +
                ", amount=" + amount +
                ", standard=" + standard +
                ", hours=" + hours +
                '}';
    }

    public void computeSimilar(Payment fee) {
        Double amountSim = Math.min(this.amount, fee.getAmount()) / (double) Math.max(this.amount, fee.getAmount());
        Double hoursSim = Math.min(this.hours, fee.getHours()) / (double) Math.max(this.hours, fee.getHours());
        Double standardSim = Math.min(this.standard, fee.getStandard()) / Math.max(this.standard, fee.getStandard());
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = amountSim + hoursSim + standardSim + textSim;
    }
}