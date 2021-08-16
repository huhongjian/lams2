package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算报警消息内容
 */
@Data
public class MessageContent implements Serializable {
    private Long id;
    private String toEmail;
    private String projectName;
    private String planName;
    private String itemTypeName;
    /**
     * 当前执行率
     */
    private String executeRate;

    /**
     * 期望执行率
     */
    private String needExecuteRate;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createTime;
}
