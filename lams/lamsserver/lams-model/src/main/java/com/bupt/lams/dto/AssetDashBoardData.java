package com.bupt.lams.dto;

import lombok.Data;

/**
 * 资产仪表盘顶部表格数据
 * 以及折线图数据行
 */
@Data
public class AssetDashBoardData {
    private String date;
    private Long total = 0L;
    private Long inUse = 0L;
    private Long free = 0L;
    private Long inRepair = 0L;
    private Double money = 0D;
    private Long cleaned = 0L;
}
