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
    /**
     * 资产流程类型：1.入库，2.出库
     */
    private int category;
    private String type;
    private Long did;
    private String brand;
    private String status;
    private Double price;
    /**
     * 申请人
     */
    private String applicant;
    /**
     * 申请人邮箱
     */
    private String applicantEmail;
    /**
     * 申请人电话
     */
    private String applicantPhone;
    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date readyDate;
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
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date applyDate;
    /**
     * 预计借用时间
     */
    private Integer duration;
    /**
     * 申请理由
     */
    private String reason;
    /**
     * 详细信息
     */
    private Object adv;

    public void setChargerByApplicant() {
        charger = applicant;
        chargerEmail = applicantEmail;
        chargerPhone = applicantPhone;
    }
}