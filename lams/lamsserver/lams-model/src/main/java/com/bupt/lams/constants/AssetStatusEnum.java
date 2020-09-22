package com.bupt.lams.constants;

/**
 * 资产状态
 */
public enum AssetStatusEnum {
    CREATE("申请采购", 0),
    APPROVE("审批通过", 1),
    READY("已入库", 2),
    ASK("申请借用", 3),
    OCCUPIED("已借出", 4),
    TRANSFER("转交", 5);

    private String name;
    private int index;

    AssetStatusEnum(String name, int index) {
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