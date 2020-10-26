package com.bupt.lams.constants;

/**
 * 操作记录类型
 */
public enum RecordAopDispatchEnum {
    UPDATE_ASSET("修改资产信息", 1),
    CHANGE_ASSET_STATUS("修改资产状态", 2),
    ADD_ASSET("资产采购申请", 3),
    BORROW_ASSET("资产借用申请", 4),
    UPDATE_ORDER("编辑工单信息", 5),
    DELETE_ORDERS("删除工单", 6),
    CANCEL("取消工单", 7),
    HANDLE("处理工单", 8);

    private String name;
    private int index;

    RecordAopDispatchEnum(String name, int index) {
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
        for (RecordAopDispatchEnum e : RecordAopDispatchEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
