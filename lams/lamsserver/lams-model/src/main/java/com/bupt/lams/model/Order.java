package com.bupt.lams.model;

import com.bupt.lams.constants.ProcessTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工单信息pojo
 */
@Data
public class Order implements Serializable {
    private Long id;
    /**
     * 流程类型：1.入库，2.出库，3.离退
     */
    private Integer category;
    /**
     * 流程类型：1.入库，2.出库，3.离退
     */
    private String categoryName;
    /**
     * 状态
     */
    private String status;
    /**
     * 预计借用时间
     */
    private Integer duration;
    /**
     * 申请理由
     */
    private String reason;
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
     * 关联的资产信息
     */
    private Asset asset;

    public void setCategory(int category) {
        this.category = category;
        this.categoryName = ProcessTypeEnum.getNameByIndex(category);
    }
}