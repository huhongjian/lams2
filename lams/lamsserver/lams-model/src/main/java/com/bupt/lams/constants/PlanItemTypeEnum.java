package com.bupt.lams.constants;

/**
 * 预算明细类型
 */
public enum PlanItemTypeEnum {
    EQUIPMENT("设备费", 1),
    MATERIAL("材料费", 2),
    TESTING("测试化验加工费", 3),
    MEETING("会议费", 4),
    RESEARCH("课题调研差旅费", 5),
    ACADEMIC("国内学术会议差旅费", 6),
    TRANSPORTATION("北京市内交通费", 7),
    PUBLISH("出版/文献/信息传播/知识产权事务费", 8),
    PAYMENT("劳务费", 9),
    ADVISORY("专家咨询费", 10),
    OTHERS("其他支出", 11);

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

    /**
     * 根据name获取去index
     */
    public static Integer getIndexByName(String name) {
        for (PlanItemTypeEnum e : PlanItemTypeEnum.values()) {
            if (name.equals(e.getName())) {
                return e.getIndex();
            }
        }
        return null;
    }
}
