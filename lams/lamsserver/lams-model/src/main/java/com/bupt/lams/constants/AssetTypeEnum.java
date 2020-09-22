package com.bupt.lams.constants;

/**
 * 资产类型
 */
public enum AssetTypeEnum {
    MOBILE("手机", 0),
    PC("主机", 1),
    SWITCH("交换机", 2),
    RANGE("测距仪", 3);

    private String name;
    private int index;

    AssetTypeEnum(String name, int index) {
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
