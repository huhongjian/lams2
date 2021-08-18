package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.TestingFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.MaterialFee;
import com.bupt.lams.model.planItem.ResearchFee;
import com.bupt.lams.model.planItem.TestingFee;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
import com.bupt.lams.utils.UserInfoUtils;
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

    @Transactional(rollbackFor = Exception.class)
    public Long addTestingFee(TestingFee testingFee) {
        testingFee.setItemType(PlanItemTypeEnum.TESTING.getIndex());
        testingFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
        // 计算合计
        if (testingFee.getSubTotal() == null) {
            testingFee.setSubTotal(testingFee.getUnitPrice() * testingFee.getTimes());
        }
        testingFee.setUnfulfilledQuota(testingFee.getSubTotal());
        testingFee.setExecuteRate(ComputeUtils.computeExecuteRate(testingFee.getSubTotal(), testingFee.getUnfulfilledQuota()));
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

    public void update(TestingFee testingFee) {
        testingFeeMapper.update(testingFee);
    }

    public TestingFee getById(Long id) {
        return testingFeeMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        testingFeeMapper.clear(user.getUsername());
    }
}
