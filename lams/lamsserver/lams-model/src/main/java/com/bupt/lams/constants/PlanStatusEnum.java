package com.bupt.lams.constants;

/**
 * 预算状态类型
 */
public enum PlanStatusEnum {
    ON("进行中", 1),
    END("已完结", 2);

    private String name;
    private int index;

    PlanStatusEnum(String name, int index) {
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
        for (PlanStatusEnum e : PlanStatusEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
