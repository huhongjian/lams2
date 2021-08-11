package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.OtherFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.MeetingFee;
import com.bupt.lams.model.planItem.OtherFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试化验加工费service
 */
@Service
public class OtherFeeService {
    @Resource
    OtherFeeMapper otherFeeMapper;

    public List<OtherFee> getOtherFeeList() {
        return otherFeeMapper.getOtherFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addOtherFee(OtherFee otherFee) {
        otherFee.setItemType(PlanItemTypeEnum.OTHERS.getIndex());
        // 计算合计
        if (otherFee.getSubTotal() == null) {
            otherFee.setSubTotal(0.0);
        }
        otherFeeMapper.insertSelective(otherFee);
        return otherFee.getId();
    }

    public List<OtherFee> getOtherFeeByPlanAndItem(List<PlanAndItem> planAndItemList) {
        List<OtherFee> res = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return res;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.OTHERS.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return res;
        }
        return otherFeeMapper.getOtherFeeByIds(idList);
    }

    public List<OtherFee> getOtherFeeListByIds(List<Long> pids) {
        return otherFeeMapper.getOtherFeeByIds(pids);
    }
}
