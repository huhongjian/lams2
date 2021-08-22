package com.bupt.lams.dto;

import com.bupt.lams.model.planItem.PlanItem;
import lombok.Data;

import java.io.Serializable;
import java.text.DecimalFormat;
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

    public void setFunding(Double funding) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.funding = new Double(df.format(funding.doubleValue()));
    }

    public Double getFunding() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (this.funding == null) {
            return null;
        }
        return new Double(df.format(this.funding.doubleValue()));
    }

    public void setUnfulfilledQuota(Double unfulfilledQuota) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.unfulfilledQuota = new Double(df.format(unfulfilledQuota.doubleValue()));
    }

    public Double getUnfulfilledQuota() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (this.unfulfilledQuota == null) {
            return null;
        }
        return new Double(df.format(this.unfulfilledQuota.doubleValue()));
    }
}