package com.bupt.lams.dto;

import com.bupt.lams.model.planItem.PlanItem;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 预算明细信息dto
 */
@Data
public class PlanItemData implements Serializable {
    /**
     * 总额度
     */
    private Double funding;

    /**
     * 未执行额度
     */
    private Double unfulfilledQuota;

    /**
     * 执行率
     */
    private Double executeRate;

    /**
     * 预算明细列表
     */
    List<PlanItem> planItemList;

    public PlanItemData() {
        this.funding = 0.0;
        this.unfulfilledQuota = 0.0;
        this.executeRate = 0.0;
        this.planItemList = new ArrayList<>();
    }
}