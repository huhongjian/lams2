package com.bupt.lams.dto;

import lombok.Data;

/**
 * 科研项目顶部表格数据
 */
@Data
public class ProjectHeadTableData {
    private Double total = 0D;
    private Double used = 0D;
    private Double left = 0D;
    private Double inPlan = 0D;
    private Double executeRate = 0D;
}
