package com.bupt.lams.model;

import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 操作记录
 */
@Data
public class Record {
    private Long id;

    private Integer operate;

    private String operateName;

    private String operatorName;

    private String operatorMail;

    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date operateTime;

    public void setOperate(Integer operate) {
        this.operate = operate;
        this.operateName = RecordAopDispatchEnum.getNameByIndex(operate);
    }

    public void setOperator(LamsUser user) {
        this.operatorName = user.getName();
        this.operatorMail = user.getUsername();
    }
}
