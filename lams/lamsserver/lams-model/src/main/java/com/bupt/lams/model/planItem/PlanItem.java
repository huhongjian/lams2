package com.bupt.lams.model.planItem;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算明细信息pojo
 */
@Data
public class PlanItem implements Serializable {
    private Long id;
    private String userEmail;
    /**
     * 预算明细类型
     */
    private Integer itemType;
    private String itemTypeName;

    /**
     * 预算说明
     */
    private String remark;

    /**
     * 合计（万元）
     */
    private Double subTotal;

    /**
     * 未执行额度（万元）
     */
    private Double unfulfilledQuota;

    /**
     * 执行率
     */
    private Double executeRate;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date updateTime;

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
        this.itemTypeName = PlanItemTypeEnum.getNameByIndex(itemType);
    }
}