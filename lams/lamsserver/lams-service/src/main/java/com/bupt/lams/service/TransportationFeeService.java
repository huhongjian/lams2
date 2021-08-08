package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.TransportationFeeMapper;
import com.bupt.lams.model.planItem.PlanItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 北京市内交通费service
 */
@Service
public class TransportationFeeService {
    @Resource
    TransportationFeeMapper transportationFeeMapper;

    public List<PlanItem> getTransportationFeeList() {
        return transportationFeeMapper.getTransportationFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addTransportationFee(PlanItem transportationFee) {
        transportationFee.setItemType(PlanItemTypeEnum.MEETING.getIndex());
        // 计算合计
        if (transportationFee.getSubTotal() == null) {
            transportationFee.setSubTotal(0.0);
        }
        transportationFeeMapper.insertSelective(transportationFee);
        return transportationFee.getId();
    }
}
