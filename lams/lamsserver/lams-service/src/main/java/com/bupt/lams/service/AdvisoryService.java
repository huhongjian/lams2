package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.AdvisoryMapper;
import com.bupt.lams.model.planItem.Advisory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 专家咨询费service
 */
@Service
public class AdvisoryService {
    @Resource
    AdvisoryMapper advisoryMapper;

    public List<Advisory> getAdvisoryList() {
        return advisoryMapper.getAdvisoryList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addAdvisory(Advisory testingFee) {
        testingFee.setItemType(PlanItemTypeEnum.ADVISORY.getIndex());
        // 计算合计
        if (testingFee.getSubTotal() == null) {
            testingFee.setSubTotal(testingFee.getStandard() * testingFee.getDays());
        }
        advisoryMapper.insertSelective(testingFee);
        return testingFee.getId();
    }
}
