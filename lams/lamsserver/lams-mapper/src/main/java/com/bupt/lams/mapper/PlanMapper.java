package com.bupt.lams.mapper;

import com.bupt.lams.dto.PlanQueryCondition;
import com.bupt.lams.model.Plan;

import java.util.List;

public interface PlanMapper {
    /**
     * 根据条件获取预算计划信息
     *
     * @param condition
     * @return
     */
    List<Plan> getPlanByCondition(PlanQueryCondition condition);

    List<Plan> getAllPlans();

    int insertSelective(Plan plan);

    void updatePlan(Plan plan);

    Plan getPlanById(Long id);

    Double getTotal();

    Double getTotalByProjectId(Long id);

    List<Plan> getPlansByProjectIds(List<Long> pids);

    void deletePlans(List<Long> ids);
}
