package com.bupt.lams.constants;

/**
 * 操作类型
 */
public enum OperateTypeEnum {
    CREATE("创建申请", 0),
    REJECT("拒绝", 1),
    APPROVE("通过", 2),
    IN("入库", 3),
    TRANSFER("转交", 4),
    CANCEL("取消", 5);

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