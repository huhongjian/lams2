package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.PublishFee;

import java.util.List;

public interface PublishFeeMapper {

    List<PublishFee> getPublishFeeList();

    int insertSelective(PublishFee publishFee);

    List<PublishFee> getPublishFeeByIds(List<Long> ids);
}
