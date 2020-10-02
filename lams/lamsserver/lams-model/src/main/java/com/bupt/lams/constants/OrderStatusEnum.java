package com.bupt.lams.constants;

/**
 * 工单状态
 */
public enum OrderStatusEnum {
    CREATE("申请采购", 0),
    APPROVE("审批通过", 1),
    READY("已入库", 2),
    ASK("申请借用", 3),
    OCCUPIED("已借出", 4),
    TRANSFER("申请转交", 5),
    REJECTED("审批未通过", 6);

    private String name;
    private int index;

    OrderStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
