package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.mapper.TestingFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.TestingFee;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 测试化验加工费service
 */
@Service
public class TestingFeeService {
    @Resource
    TestingFeeMapper testingFeeMapper;
    @Resource
    BudgetRecordMapper budgetRecordMapper;

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
        List<TestingFee> testingFeeList = testingFeeMapper.getFullInfoByIds(idList);
        data = TransferUtils.transToPlanItemData(new ArrayList<>(testingFeeList));
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

    public List<TestingFee> getRecommendFee() {
        // 查询所有设备费信息
        List<TestingFee> testingFeeList = testingFeeMapper.getAll();
        if (CollectionUtils.isEmpty(testingFeeList)) {
            return new ArrayList<>();
        }
        // 获取最近一条设备费执行记录
        Long id = budgetRecordMapper.getRecentIdByItemType(PlanItemTypeEnum.TESTING.getIndex());
        if (id == null) {
            return testingFeeList.size() > 10 ? testingFeeList.subList(0, 10) : testingFeeList;
        }
        TestingFee fee = testingFeeMapper.getById(id);
        if (fee == null) {
            return testingFeeList.size() > 10 ? testingFeeList.subList(0, 10) : testingFeeList;
        }
        // 计算相似度，并按照相似度逆序排列
        for (TestingFee e : testingFeeList) {
            e.computeSimilar(fee);
        }
        Collections.sort(testingFeeList, Comparator.comparingDouble(x -> -x.getSimilar()));
        return testingFeeList.size() > 10 ? testingFeeList.subList(0, 10) : testingFeeList;
    }
}
