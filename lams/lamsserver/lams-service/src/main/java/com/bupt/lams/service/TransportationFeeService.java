package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.TransportationFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.PlanItem;
import com.bupt.lams.model.planItem.TestingFee;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
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
        transportationFee.setUnfulfilledQuota(transportationFee.getSubTotal());
        transportationFee.setExecuteRate(ComputeUtils.computeExecuteRate(transportationFee.getSubTotal(), transportationFee.getUnfulfilledQuota()));
        transportationFeeMapper.insertSelective(transportationFee);
        return transportationFee.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.TRANSPORTATION.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        data = getTransportationFeeDataByIds(idList);
        return data;
    }

    public PlanItemData getTransportationFeeDataByIds(List<Long> ids) {
        List<PlanItem> transportationFeeList = transportationFeeMapper.getTransportationFeeByIds(ids);
        return TransferUtils.transToPlanItemData(transportationFeeList);
    }

    public List<PlanItem> getTransportationFeeListByIds(List<Long> pids) {
        return transportationFeeMapper.getTransportationFeeByIds(pids);
    }
}
