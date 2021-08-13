package com.bupt.lams.model.planItem;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备费信息pojo
 */
@Data
public class EquipmentFeeData implements Serializable {
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
    List<EquipmentFee> equipmentFeeList;

    public EquipmentFeeData() {
        this.funding = 0.0;
        this.unfulfilledQuota = 0.0;
        this.executeRate = 0.0;
        this.equipmentFeeList = new ArrayList<>();
    }
}