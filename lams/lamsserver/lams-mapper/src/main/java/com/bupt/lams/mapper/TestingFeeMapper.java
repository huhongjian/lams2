package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.TestingFee;

import java.util.List;

public interface TestingFeeMapper {

    List<TestingFee> getTestingFeeList();

    int insertSelective(TestingFee testingFee);

    List<TestingFee> getTestingFeeByIds(List<Long> ids);
}
