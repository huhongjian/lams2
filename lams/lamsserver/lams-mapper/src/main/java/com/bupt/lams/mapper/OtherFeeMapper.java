package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.OtherFee;

import java.util.List;

public interface OtherFeeMapper {
    int insertSelective(OtherFee otherFee);

    List<OtherFee> getOtherFeeByIds(List<Long> ids);

    OtherFee getById(Long id);

    void update(OtherFee otherFee);

    void clear(String userEmail);
}
