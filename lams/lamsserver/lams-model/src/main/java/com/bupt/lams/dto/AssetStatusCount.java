package com.bupt.lams.dto;

import lombok.Data;

/**
 * 资产按状态统计数量
 */
@Data
public class AssetStatusCount {
    private Integer status;
    private String statusName;
    private Long count;
    private Double money;

    public AssetStatusCount() {
    }

    public AssetStatusCount(Integer status, String statusName, Long count, Double money) {
        this.status = status;
        this.statusName = statusName;
        this.count = count;
        this.money = money;
    }
}
