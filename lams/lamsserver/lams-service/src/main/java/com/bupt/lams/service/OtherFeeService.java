package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.OtherFeeMapper;
import com.bupt.lams.model.planItem.OtherFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
}
