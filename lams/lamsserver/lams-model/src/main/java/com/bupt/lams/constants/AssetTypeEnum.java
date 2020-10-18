package com.bupt.lams.constants;

/**
 * 资产类型
 */
public enum AssetTypeEnum {
    MOBILE("手机", 1),
    PC("主机", 2),
    SWITCH("交换机", 3),
    RANGE("测距仪", 4);

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

    /**
     * 根据index获取去name
     *
     * @param index
     * @return
     */
    public static String getNameByIndex(Integer index) {
        for (AssetTypeEnum e : AssetTypeEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
