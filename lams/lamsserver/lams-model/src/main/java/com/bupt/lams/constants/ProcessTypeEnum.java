package com.bupt.lams.constants;

/**
 * 流程类型
 */
public enum ProcessTypeEnum {
    IN("入库", 1),
    OUT("出库", 2),
    LEAVE("离退", 3),
    RETURN("归还", 4);

    private String name;
    private int index;

    ProcessTypeEnum(String name, int index) {
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
        for (ProcessTypeEnum e : ProcessTypeEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
