package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.TestingFee;

import java.util.List;

public interface TestingFeeMapper {
    int insertSelective(TestingFee testingFee);

    List<TestingFee> getTestingFeeByIds(List<Long> ids);

    TestingFee getById(Long id);

    void update(TestingFee equipmentFee);

    void clear(String userEmail);
}
