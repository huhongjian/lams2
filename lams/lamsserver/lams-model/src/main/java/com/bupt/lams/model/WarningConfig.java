package com.bupt.lams.model;

import com.bupt.lams.constants.ConfigStatusEnum;
import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.constants.WarningFreqEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 预算配置信息
 */
@Data
public class WarningConfig {
    private Long id;
    private Long projectId;
    private String projectName;
    private Long planId;
    private String planName;
    private Integer itemType;
    private String itemTypeName;
    private Double executeRate;
    private Integer configStatus;
    private String configStatusName;
    private String userEmail;
    private List<String> toUser;
    private String toUserString;

    Date[] dateScope;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date endTime;

    private Integer freq;
    private String freqName;

    private String advice;

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

    public void setConfigStatus(Integer configStatus) {
        this.configStatus = configStatus;
        this.configStatusName = ConfigStatusEnum.getNameByIndex(configStatus);
    }

    public void setFreq(Integer freq) {
        this.freq = freq;
        this.freqName = WarningFreqEnum.getNameByIndex(freq);
    }

    public void setDateScope(Date[] dateScope) {
        this.dateScope = dateScope;
        if (dateScope == null || dateScope.length != 2) {
            return;
        }
        this.startTime = dateScope[0];
        this.endTime = dateScope[1];
    }

    public void setToUser(List<String> toUser) {
        this.toUser = toUser;
        if (CollectionUtils.isEmpty(toUser)) {
            return;
        }
        String str = "";
        for (String user : toUser) {
            str += user;
            str += ",";
        }
        this.toUserString = str.substring(0, str.length() - 1);
    }

    public void setToUserString(String toUserString) {
        this.toUserString = toUserString;
        String[] userEmailList = toUserString.split(",");
        this.toUser = Arrays.asList(userEmailList);
    }
}
