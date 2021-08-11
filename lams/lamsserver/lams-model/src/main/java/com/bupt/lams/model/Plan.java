package com.bupt.lams.model;

import com.bupt.lams.constants.PlanStatusEnum;
import com.bupt.lams.model.planItem.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    List<AcademicFee> academicFeeList;
    List<Advisory> advisoryList;
    List<EquipmentFee> equipmentFeeList;
    List<MaterialFee> materialFeeList;
    List<MeetingFee> meetingFeeList;
    List<OtherFee> otherFeeList;
    List<Payment> paymentList;
    List<PlanItem> transFeeList;
    List<PublishFee> publishFeeList;
    List<ResearchFee> researchFeeList;
    List<TestingFee> testingFeeList;

    public void setStatus(Integer status) {
        this.planStatus = status;
        this.planStatusName = PlanStatusEnum.getNameByIndex(status);
    }
}