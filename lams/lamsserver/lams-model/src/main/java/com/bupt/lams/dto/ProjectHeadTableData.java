package com.bupt.lams.dto;

import java.text.DecimalFormat;

/**
 * 科研项目顶部表格数据
 */

public class ProjectHeadTableData {
    private Double total = 0D;
    private Double used = 0D;
    private Double left = 0D;
    private Double inPlan = 0D;
    private Double executeRate = 0D;

    public Double getTotal() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (this.total == null) {
            return null;
        }
        return new Double(df.format(this.total.doubleValue()));
    }

    public void setTotal(Double total) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.total = new Double(df.format(total.doubleValue()));
    }

    public Double getUsed() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (this.used == null) {
            return null;
        }
        return new Double(df.format(this.used.doubleValue()));
    }

    public void setUsed(Double used) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.used = new Double(df.format(used.doubleValue()));
    }

    public Double getLeft() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (this.left == null) {
            return null;
        }
        return new Double(df.format(this.left.doubleValue()));
    }

    public void setLeft(Double left) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.left = new Double(df.format(left.doubleValue()));
    }

    public Double getInPlan() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (this.inPlan == null) {
            return null;
        }
        return new Double(df.format(this.inPlan.doubleValue()));
    }

    public void setInPlan(Double inPlan) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.inPlan = new Double(df.format(inPlan.doubleValue()));
    }

    public Double getExecuteRate() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (this.executeRate == null) {
            return null;
        }
        return new Double(df.format(this.executeRate.doubleValue()));
    }

    public void setExecuteRate(Double executeRate) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.executeRate = new Double(df.format(executeRate.doubleValue()));
    }
}
