package com.bupt.lams.constants;

/**
 * 预算执行报销情况
 */
public enum BudgetReimburseEnum {
    NO("未报销", 0),
    YES("已报销", 1);

    private String name;
    private int index;

    BudgetReimburseEnum(String name, int index) {
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
        for (BudgetReimburseEnum e : BudgetReimburseEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
