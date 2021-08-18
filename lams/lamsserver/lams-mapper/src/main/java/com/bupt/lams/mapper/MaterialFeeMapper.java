package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.MaterialFee;

import java.util.List;

public interface MaterialFeeMapper {
    int insertSelective(MaterialFee materialFee);

    List<MaterialFee> getMaterialFeeByIds(List<Long> ids);

    MaterialFee getById(Long id);

    void update(MaterialFee materialFee);

    void clear(String userEmail);
}
