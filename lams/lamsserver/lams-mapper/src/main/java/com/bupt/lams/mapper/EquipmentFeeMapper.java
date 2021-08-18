package com.bupt.lams.mapper;

import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.ProjectQueryCondition;
import com.bupt.lams.model.planItem.Advisory;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.Project;

import java.util.List;

public interface EquipmentFeeMapper {
    /**
     * 根据条件获取科研项目信息
     *
     * @param condition
     * @return
     */
    List<Project> getProjectByCondition(ProjectQueryCondition condition);

    List<EquipmentFee> getEquipmentFeeList();

    int insertSelective(EquipmentFee equipmentFee);

    List<EquipmentFee> getEquipmentFeeByIds(List<Long> ids);

    EquipmentFee getById(Long id);

    void update(EquipmentFee equipmentFee);
}
