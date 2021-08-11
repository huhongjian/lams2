package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.TransportationFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.PlanItem;
import com.bupt.lams.model.planItem.TestingFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        transportationFee.setItemType(PlanItemTypeEnum.TRANSPORTATION.getIndex());
        // 计算合计
        if (transportationFee.getSubTotal() == null) {
            transportationFee.setSubTotal(0.0);
        }
        transportationFeeMapper.insertSelective(transportationFee);
        return transportationFee.getId();
    }

    public List<PlanItem> getTransportationFeeByPlanAndItem(List<PlanAndItem> planAndItemList) {
        List<PlanItem> res = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return res;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.TRANSPORTATION.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return res;
        }
        return transportationFeeMapper.getTransportationFeeByIds(idList);
    }

    public List<PlanItem> getTransportationFeeListByIds(List<Long> pids) {
        return transportationFeeMapper.getTransportationFeeByIds(pids);
    }
}
