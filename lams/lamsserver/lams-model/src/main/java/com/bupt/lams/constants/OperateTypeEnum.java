package com.bupt.lams.constants;

/**
 * 操作类型
 */
public enum OperateTypeEnum {
    CREATE("申请采购", 1),
    APPROVE("批准采购", 2),
    REJECT("拒绝", 3),
    IN("入库", 4),
    CONFIRM("批准借用", 6),
    CANCEL("撤回", 7),
    REFUSE("拒绝", 8),
    RETURN("确认归还", 9),
    TURN_DOWN("驳回", 10),
    STU_OUT_APPROVE("离退审批通过", 11),
    STU_OUT_REJECT("离退审批拒绝", 12);

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

    /**
     * 根据index获取去name
     *
     * @param index
     * @return
     */
    public static String getNameByIndex(Integer index) {
        for (OperateTypeEnum e : OperateTypeEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
