package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 材料费信息pojo
 */
@Data
public class MaterialFee extends PlanItem implements Serializable {
    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 规格
     */
    private String specification;

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

    /**
     * 单位
     */
    private String unit;

    private Double similar;

    @Override
    public String toString() {
        return "MaterialFee{" +
                "materialName='" + materialName + '\'' +
                ", specification='" + specification + '\'' +
                ", usage='" + usage + '\'' +
                ", amount=" + amount +
                ", unitPrice=" + unitPrice +
                ", unit='" + unit + '\'' +
                '}';
    }

    public void computeSimilar(MaterialFee fee) {
        Double amountSim = Math.min(this.amount, fee.getAmount()) / (double) Math.max(this.amount, fee.getAmount());
        Double unitPriceSim = Math.min(this.unitPrice, fee.getUnitPrice()) / Math.max(this.unitPrice, fee.getUnitPrice());
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = amountSim + unitPriceSim + textSim;
    }
}