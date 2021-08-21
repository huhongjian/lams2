package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.TestingFee;

import java.util.List;

public interface TestingFeeMapper {
    int insertSelective(TestingFee testingFee);

    List<TestingFee> getTestingFeeByIds(List<Long> ids);

    /**
     * 根据ids获取完整信息，用于执行预算明细
     */
    List<TestingFee> getFullInfoByIds(List<Long> ids);

    TestingFee getById(Long id);

    void update(TestingFee equipmentFee);

    void clear(String userEmail);

    List<TestingFee> getAll();
}
