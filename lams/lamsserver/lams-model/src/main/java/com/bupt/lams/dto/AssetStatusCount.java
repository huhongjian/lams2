package com.bupt.lams.dto;

import lombok.Data;

/**
 * 资产按状态统计数量
 */
@Data
public class AssetStatusCount {
    private Integer status;
    private Long count;
}
