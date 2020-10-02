package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实验室资产信息pojo
 */
@Data
public class Asset implements Serializable {
    private Long id;
    private String type;
    private String brand;
    private Double price;
    /**
     * 负责人
     */
    private String charger;
    /**
     * 负责人邮箱
     */
    private String chargerEmail;
    /**
     * 负责人电话
     */
    private String chargerPhone;
    /**
     * 详细信息
     */
    private Object adv;

    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date readyDate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date updateTime;
}