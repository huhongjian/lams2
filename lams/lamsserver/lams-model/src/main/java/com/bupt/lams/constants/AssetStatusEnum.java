package com.bupt.lams.constants;

/**
 * 资产状态
 */
public enum AssetStatusEnum {
    CREATE("申请中", 1),
    FREE("闲置", 2),
    BROKEN("故障", 3),
    CLEANED("报废", 4),
    REJECTED("审批未通过", 5),
    INUSE("使用中", 6);

    private String name;
    private int index;

    AssetStatusEnum(String name, int index) {
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
        for (AssetStatusEnum e : AssetStatusEnum.values()) {
            if (index.equals(e.getIndex())) {
                return e.getName();
            }
        }
        return null;
    }
}
