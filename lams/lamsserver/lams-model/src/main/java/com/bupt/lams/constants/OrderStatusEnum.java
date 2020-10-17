package com.bupt.lams.constants;

/**
 * 工单状态
 */
public enum OrderStatusEnum {
    CREATE("申请采购", 1),
    APPROVE("审批通过", 2),
    READY("已入库", 3),
    ASK("申请借用", 4),
    OCCUPIED("已借出", 5),
    REJECTED("审批未通过", 6),
    CLOSED("已关闭", 7),
    REFUSED("审批未通过", 8);

    private String name;
    private int index;

    OrderStatusEnum(String name, int index) {
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
        for (OrderStatusEnum e : OrderStatusEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
