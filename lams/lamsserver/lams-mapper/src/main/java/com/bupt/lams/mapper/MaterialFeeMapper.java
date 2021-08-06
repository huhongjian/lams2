package com.bupt.lams.mapper;

import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.ProjectQueryCondition;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.MaterialFee;

import java.util.List;

public interface MaterialFeeMapper {
    /**
     * 根据条件获取科研项目信息
     *
     * @param condition
     * @return
     */
    List<Project> getProjectByCondition(ProjectQueryCondition condition);

    List<MaterialFee> getMaterialFeeList();
    /**
     * 获取符合条件的资产总数
     *
     * @param condition
     * @return
     */
    Long getTotalByCondition(AssetQueryCondition condition);

    int insertSelective(MaterialFee materialFee);

    void updateProject(Project project);
}
