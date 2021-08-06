package com.bupt.lams.constants;

/**
 * 预算明细类型
 */
public enum PlanItemTypeEnum {
    EQUIPMENT("设备费", 1),
    MATERIAL("材料费", 2),
    TESTING("测试化验加工费", 3),
    MEETING("差旅/会议/国际合作与交流费", 4),
    PUBLISH("出版/文献/信息传播/知识产权事务费", 5),
    PAYMENT("劳务费", 6),
    ADVISORY("专家咨询费", 7),
    OTHERS("其他支出", 8);

    private String name;
    private int index;

    PlanItemTypeEnum(String name, int index) {
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
        for (PlanItemTypeEnum e : PlanItemTypeEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
