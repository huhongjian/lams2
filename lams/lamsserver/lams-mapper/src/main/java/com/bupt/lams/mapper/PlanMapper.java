package com.bupt.lams.mapper;

import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.PlanQueryCondition;
import com.bupt.lams.dto.ProjectQueryCondition;
import com.bupt.lams.model.Plan;
import com.bupt.lams.model.Project;

import java.util.List;

public interface PlanMapper {
    /**
     * 根据条件获取预算计划信息
     *
     * @param condition
     * @return
     */
    List<Plan> getPlanByCondition(PlanQueryCondition condition);

    /**
     * 获取符合条件的资产总数
     *
     * @param condition
     * @return
     */
    Long getTotalByCondition(AssetQueryCondition condition);

    int insertSelective(Plan plan);

    void updateProject(Project project);
}
