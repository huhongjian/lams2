package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 科研项目信息pojo
 */
@Data
public class Project implements Serializable {
    private Long id;
    private String projectName;
    private String projectType;
    private String userEmail;

    /**
     * 用户
     */
    private LamsUser user;

    /**
     * 项目总体执行年限
     */
    private Integer years;

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
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date updateTime;
}