package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 工单资产关联信息
 */
@Data
public class OrderAsset {
    private Long id;
    private Long oid;
    private Long aid;
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

    public OrderAsset() {
        super();
    }

    public OrderAsset(Long oid, Long aid, Date createTime, Date updateTime) {
        this.oid = oid;
        this.aid = aid;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
