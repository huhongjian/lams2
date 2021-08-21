package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.EquipmentFee;

import java.util.List;

public interface EquipmentFeeMapper {
    int insertSelective(EquipmentFee equipmentFee);

    /**
     * 根据ids获取设备费基本信息，用于新建预算计划明细，那时和预算计划以及科研项目还没有关联关系
     */
    List<EquipmentFee> getEquipmentFeeByIds(List<Long> ids);

    /**
     * 根据ids获取设备费完整信息，用于执行预算明细
     */
    List<EquipmentFee> getFullInfoByIds(List<Long> ids);

    EquipmentFee getById(Long id);

    void update(EquipmentFee equipmentFee);

    void clear(String userEmail);

    List<EquipmentFee> getByPlanId(Long pid);

    List<EquipmentFee> getAll();
}
