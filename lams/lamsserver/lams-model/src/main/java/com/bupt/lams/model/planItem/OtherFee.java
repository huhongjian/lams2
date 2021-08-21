package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 其他支出pojo
 */
@Data
public class OtherFee extends PlanItem implements Serializable {
    /**
     * 类别
     */
    private String type;

    private Double similar;

    @Override
    public String toString() {
        return "OtherFee{" +
                "type='" + type + '\'' +
                '}';
    }

    public void computeSimilar(OtherFee fee) {
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = textSim;
    }
}