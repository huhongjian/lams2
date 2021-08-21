package com.bupt.lams.model.planItem;

import com.bupt.lams.utils.ComputeUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 出版/文献/信息传播/知识产权事务费pojo
 */
@Data
public class PublishFee extends PlanItem implements Serializable {
    /**
     * 类别
     */
    private String type;

    private Double similar;

    @Override
    public String toString() {
        return "PublishFee{" +
                "type='" + type + '\'' +
                '}';
    }

    public void computeSimilar(PublishFee fee) {
        Double textSim = ComputeUtils.cos(this.toString(), fee.toString());
        this.similar = textSim;
    }
}