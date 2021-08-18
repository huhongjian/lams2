package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.EquipmentFee;

import java.util.List;

public interface EquipmentFeeMapper {
    int insertSelective(EquipmentFee equipmentFee);

    List<EquipmentFee> getEquipmentFeeByIds(List<Long> ids);

    EquipmentFee getById(Long id);

    void update(EquipmentFee equipmentFee);

    void clear(String userEmail);
}
