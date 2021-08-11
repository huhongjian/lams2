package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.PublishFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.Payment;
import com.bupt.lams.model.planItem.PublishFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试化验加工费service
 */
@Service
public class PublishFeeService {
    @Resource
    PublishFeeMapper publishFeeMapper;

    public List<PublishFee> getPublishFeeList() {
        return publishFeeMapper.getPublishFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addPublishFee(PublishFee publishFee) {
        publishFee.setItemType(PlanItemTypeEnum.PUBLISH.getIndex());
        // 计算合计
        if (publishFee.getSubTotal() == null) {
            publishFee.setSubTotal(0.0);
        }
        publishFeeMapper.insertSelective(publishFee);
        return publishFee.getId();
    }

    public List<PublishFee> getPublishFeeByPlanAndItem(List<PlanAndItem> planAndItemList) {
        List<PublishFee> res = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return res;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.PUBLISH.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return res;
        }
        return publishFeeMapper.getPublishFeeByIds(idList);
    }

    public List<PublishFee> getPublishFeeListByIds(List<Long> pids) {
        return publishFeeMapper.getPublishFeeByIds(pids);
    }
}
