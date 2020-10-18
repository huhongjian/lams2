package com.bupt.lams.dto;

import lombok.Data;

/**
 * 资产概况数据
 */
@Data
public class AssetDashBoardHeadTableData {
    private Long total = 0L;
    private Long inUse = 0L;
    private Long free = 0L;
    private Long inRepair = 0L;
    private Double money = 0D;
    private Long cleaned = 0L;
}
