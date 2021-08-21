package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备费信息pojo
 */
@Data
public class EquipmentFee extends PlanItem implements Serializable {
    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 用途
     */
    private String usage;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 单价
     */
    private Double unitPrice;

    private Double similar;

    @Override
    public String toString() {
        return "EquipmentFee{" +
                "equipmentName='" + equipmentName + '\'' +
                ", usage='" + usage + '\'' +
                ", amount=" + amount +
                ", unitPrice=" + unitPrice +
                '}';
    }

    public void computeSimilar(EquipmentFee fee) {
        Double amountSim = Math.min(this.amount, fee.getAmount()) / (double) Math.max(this.amount, fee.getAmount());
        Double unitPriceSim = Math.min(this.unitPrice, fee.getUnitPrice()) / Math.max(this.unitPrice, fee.getUnitPrice());
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = amountSim + unitPriceSim + textSim;
    }
}