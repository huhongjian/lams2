package com.bupt.lams.constants;

/**
 * 操作类型
 */
public enum OperateTypeEnum {
    REJECT("拒绝", 0),
    APPROVE("通过", 1),
    IN("入库", 2),
    TRANSFER("转交", 3),
    CANCEL("取消", 4);

    private String name;
    private int index;

    OperateTypeEnum(String name, int index) {
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
