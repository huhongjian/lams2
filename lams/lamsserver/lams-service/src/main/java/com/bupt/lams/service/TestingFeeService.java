package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.TestingFeeMapper;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.planItem.MaterialFee;
import com.bupt.lams.model.planItem.TestingFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试化验加工费service
 */
@Service
public class TestingFeeService {
    @Resource
    TestingFeeMapper testingFeeMapper;

    public List<TestingFee> getTestingFeeList() {
        return testingFeeMapper.getTestingFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addTestingFee(TestingFee testingFee) {
        testingFee.setItemType(PlanItemTypeEnum.TESTING.getIndex());
        // 计算合计
        if (testingFee.getSubTotal() == null) {
            testingFee.setSubTotal(testingFee.getUnitPrice() * testingFee.getTimes());
        }
        testingFeeMapper.insertSelective(testingFee);
        return testingFee.getId();
    }

    public void updateAsset(Project project) {
        testingFeeMapper.updateProject(project);
    }
}
