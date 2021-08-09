package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算计划信息pojo
 */
@Data
public class Plan implements Serializable {
    private Long id;
    private String planName;
    private Integer planStatus;
    private String planStatusName;
    private Long projectId;
    private String userEmail;

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
    private Double unFullFilledQuota;

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
}