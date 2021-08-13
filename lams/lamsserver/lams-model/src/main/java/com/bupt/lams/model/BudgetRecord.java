package com.bupt.lams.model;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算执行信息pojo
 */
@Data
public class BudgetRecord implements Serializable {
    private Long id;
    private Double funding;
    private Long projectId;
    private String projectName;
    private Long planId;
    private String planName;
    private Long planItemId;
    private Integer itemType;
    private String itemTypeName;

    /**
     * 预算执行备注
     */
    private String remark;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date executeDate;

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

    public void setType(Integer type) {
        this.itemType = type;
        this.itemTypeName = PlanItemTypeEnum.getNameByIndex(type);
    }
}