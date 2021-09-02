package com.bupt.lams.model;

import com.bupt.lams.dto.PlanItemData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 预算计划信息pojo
 */
@Data
public class Plan implements Serializable {
    private Long id;
    private Integer isDeleted;
    private String planName;
    private Long projectId;
    private String projectName;
    private String userEmail;
    private String remark;

    /**
     * 用户
     */
    private LamsUser user;

    /**
     * 资助额度
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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date updateTime;

    /**
     * 关联的预算明细
     */
    Map<String, PlanItemData> planItemDataMap = new HashMap<>();

    public void setPlanItem(String key, PlanItemData data) {
        this.planItemDataMap.put(key, data);
    }
}