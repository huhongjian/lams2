package com.bupt.lams.constants;

/**
 * 预算配置类型
 */
public enum ConfigStatusEnum {
    ON("进行中", 1),
    STOP("暂停", 2);

    private String name;
    private int index;

    ConfigStatusEnum(String name, int index) {
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
        for (ConfigStatusEnum e : ConfigStatusEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
