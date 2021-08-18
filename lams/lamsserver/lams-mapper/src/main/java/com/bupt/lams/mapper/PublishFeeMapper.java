package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.PublishFee;

import java.util.List;

public interface PublishFeeMapper {
    int insertSelective(PublishFee publishFee);

    List<PublishFee> getPublishFeeByIds(List<Long> ids);

    PublishFee getById(Long id);

    void update(PublishFee equipmentFee);

    void clear(String userEmail);
}
