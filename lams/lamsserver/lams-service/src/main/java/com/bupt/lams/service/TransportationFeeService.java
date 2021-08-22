package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.mapper.TransportationFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.PlanItem;
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
 * 北京市内交通费service
 */
@Service
public class TransportationFeeService {
    @Resource
    TransportationFeeMapper transportationFeeMapper;
    @Resource
    BudgetRecordMapper budgetRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addTransportationFee(PlanItem transportationFee) {
        transportationFee.setItemType(PlanItemTypeEnum.TRANSPORTATION.getIndex());
        transportationFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
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
        List<PlanItem> transFeeList = transportationFeeMapper.getFullInfoByIds(idList);
        data = TransferUtils.transToPlanItemData(new ArrayList<>(transFeeList));
        return data;
    }

    public PlanItemData getTransportationFeeDataByIds(List<Long> ids) {
        List<PlanItem> transportationFeeList = transportationFeeMapper.getTransportationFeeByIds(ids);
        return TransferUtils.transToPlanItemData(transportationFeeList);
    }

    public List<PlanItem> getTransportationFeeListByIds(List<Long> pids) {
        return transportationFeeMapper.getTransportationFeeByIds(pids);
    }

    public void update(PlanItem planItem) {
        transportationFeeMapper.update(planItem);
    }

    public PlanItem getById(Long id) {
        return transportationFeeMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        transportationFeeMapper.clear(user.getUsername());
    }

    public List<PlanItem> getRecommendFee() {
        // 查询所有设备费信息
        List<PlanItem> transFeeList = transportationFeeMapper.getAll();
        if (CollectionUtils.isEmpty(transFeeList)) {
            return new ArrayList<>();
        }
        // 获取最近一条设备费执行记录
        Long id = budgetRecordMapper.getRecentIdByItemType(PlanItemTypeEnum.TRANSPORTATION.getIndex(), UserInfoUtils.getLoginedUser().getUsername());
        if (id == null) {
            return transFeeList.size() > 10 ? transFeeList.subList(0, 10) : transFeeList;
        }
        PlanItem fee = transportationFeeMapper.getById(id);
        if (fee == null) {
            return transFeeList.size() > 10 ? transFeeList.subList(0, 10) : transFeeList;
        }
        // 计算相似度，并按照相似度逆序排列
        for (PlanItem e : transFeeList) {
            e.computeSimilar(fee);
        }
        Collections.sort(transFeeList, Comparator.comparingDouble(x -> -x.getSimilar()));
        return transFeeList.size() > 10 ? transFeeList.subList(0, 10) : transFeeList;
    }
}
