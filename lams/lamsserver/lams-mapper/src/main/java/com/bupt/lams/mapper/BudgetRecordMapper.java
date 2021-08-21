package com.bupt.lams.mapper;

import com.bupt.lams.dto.ExecutionQueryCondition;
import com.bupt.lams.model.BudgetRecord;

import java.util.List;

public interface BudgetRecordMapper {
    /**
     * 根据条件获取执行流水信息
     *
     * @param condition
     * @return
     */
    List<BudgetRecord> getBudgetRecordByCondition(ExecutionQueryCondition condition);

    int insertSelective(BudgetRecord budgetRecord);

    List<BudgetRecord> getByIds(List<Long> ids);

    void deleteManyByIds(List<Long> ids);

    Long getRecentIdByItemType(Integer type);
}
