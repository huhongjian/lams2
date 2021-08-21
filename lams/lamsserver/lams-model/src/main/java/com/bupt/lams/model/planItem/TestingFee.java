package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 测试化验加工费pojo
 */
@Data
public class TestingFee extends PlanItem implements Serializable {
    /**
     * 加工内容
     */
    private String content;

    /**
     * 次数
     */
    private Integer times;

    /**
     * 单价（万元）
     */
    private Double unitPrice;

    private Double similar;

    @Override
    public String toString() {
        return "TestingFee{" +
                "content='" + content + '\'' +
                ", times=" + times +
                ", unitPrice=" + unitPrice +
                '}';
    }

    public void computeSimilar(TestingFee fee) {
        Double timesSim = Math.min(this.times, fee.getTimes()) / (double) Math.max(this.times, fee.getTimes());
        Double unitPriceSim = Math.min(this.unitPrice, fee.getUnitPrice()) / Math.max(this.unitPrice, fee.getUnitPrice());
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = timesSim + unitPriceSim + textSim;
    }
}