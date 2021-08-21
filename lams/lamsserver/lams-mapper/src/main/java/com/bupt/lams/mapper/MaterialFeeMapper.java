package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.MaterialFee;

import java.util.List;

public interface MaterialFeeMapper {
    int insertSelective(MaterialFee materialFee);

    List<MaterialFee> getMaterialFeeByIds(List<Long> ids);

    /**
     * 根据ids获取设备费完整信息，用于执行预算明细
     */
    List<MaterialFee> getFullInfoByIds(List<Long> ids);

    MaterialFee getById(Long id);

    void update(MaterialFee materialFee);

    void clear(String userEmail);

    List<MaterialFee> getAll();
}
