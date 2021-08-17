package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.TestingFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.MaterialFee;
import com.bupt.lams.model.planItem.ResearchFee;
import com.bupt.lams.model.planItem.TestingFee;
import com.bupt.lams.utils.TransferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.TESTING.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        data = getTestingFeeDataByIds(idList);
        return data;
    }

    public PlanItemData getTestingFeeDataByIds(List<Long> ids) {
        List<TestingFee> testingFeeList = testingFeeMapper.getTestingFeeByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(testingFeeList));
    }

    public List<TestingFee> getTestingFeeListByIds(List<Long> pids) {
        return testingFeeMapper.getTestingFeeByIds(pids);
    }
}
