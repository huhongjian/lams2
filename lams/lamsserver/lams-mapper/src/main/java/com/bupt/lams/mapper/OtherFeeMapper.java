package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.OtherFee;

import java.util.List;

public interface OtherFeeMapper {

    List<OtherFee> getOtherFeeList();

    int insertSelective(OtherFee otherFee);
}
