package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 预算计划和预算计划明细关联信息
 */
@Data
public class PlanAndItem {
    private Long id;

    /**
     * 预算计划id
     */
    private Long pid;

    /**
     * 预算明细类型id
     */
    private Integer itid;

    /**
     * 预算明细id
     */
    private Long iid;

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

    public PlanAndItem() {
        super();
    }

    public PlanAndItem(Long pid, Integer itid, Long iid, Date createTime, Date updateTime) {
        this.pid = pid;
        this.itid = itid;
        this.iid = iid;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
