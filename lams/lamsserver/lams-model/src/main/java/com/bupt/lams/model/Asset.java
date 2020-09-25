package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 实验室资产信息pojo
 */
public class Asset implements Serializable {
    private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public String getCharger() {
        return charger;
    }

    public void setCharger(String charger) {
        this.charger = charger;
    }

    public String getChargerEmail() {
        return chargerEmail;
    }

    public void setChargerEmail(String chargerEmail) {
        this.chargerEmail = chargerEmail;
    }

    public String getChargerPhone() {
        return chargerPhone;
    }

    public void setChargerPhone(String chargerPhone) {
        this.chargerPhone = chargerPhone;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setChargerByApplicant() {
        charger = applicant;
        chargerEmail = applicantEmail;
        chargerPhone = applicantPhone;
    }
}