package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.OtherFee;

import java.util.List;

public interface OtherFeeMapper {
    int insertSelective(OtherFee otherFee);

    List<OtherFee> getOtherFeeByIds(List<Long> ids);

    /**
     * 根据ids获取完整信息，用于执行预算明细
     */
    List<OtherFee> getFullInfoByIds(List<Long> ids);

    OtherFee getById(Long id);

    void update(OtherFee otherFee);

    void clear(String userEmail);

    List<OtherFee> getAll();
}
