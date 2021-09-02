package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.mapper.OtherFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.OtherFee;
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
 * 其他支出service
 */
@Service
public class OtherFeeService {
    @Resource
    OtherFeeMapper otherFeeMapper;
    @Resource
    BudgetRecordMapper budgetRecordMapper;
    @Resource
    PlanService planService;

    @Transactional(rollbackFor = Exception.class)
    public Long addOtherFee(OtherFee otherFee) {
        otherFee.setItemType(PlanItemTypeEnum.OTHERS.getIndex());
        otherFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
        otherFee.setUnfulfilledQuota(otherFee.getSubTotal());
        otherFee.setExecuteRate(ComputeUtils.computeExecuteRate(otherFee.getSubTotal(), otherFee.getUnfulfilledQuota()));
        otherFeeMapper.insertSelective(otherFee);
        return otherFee.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.OTHERS.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        List<OtherFee> otherFeeList = otherFeeMapper.getFullInfoByIds(idList);
        data = TransferUtils.transToPlanItemData(new ArrayList<>(otherFeeList));
        return data;
    }

    public PlanItemData getOtherFeeDataByIds(List<Long> ids) {
        List<OtherFee> equipmentFeeList = otherFeeMapper.getOtherFeeByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(equipmentFeeList));
    }

    public List<OtherFee> getOtherFeeListByIds(List<Long> pids) {
        return otherFeeMapper.getOtherFeeByIds(pids);
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(OtherFee otherFee) throws Exception{
        otherFee.setItemType(PlanItemTypeEnum.OTHERS.getIndex());
        OtherFee old = otherFeeMapper.getById(otherFee.getId());
        Double used = old.getSubTotal() - old.getUnfulfilledQuota();
        if (otherFee.getSubTotal() < used) {
            throw new Exception("总额度小于已执行额度！");
        }
        // 如果总额有更新, 则更新相关数据
        if (old.getSubTotal() != otherFee.getSubTotal()) {
            Double change = otherFee.getSubTotal() - old.getSubTotal();
            double quota = otherFee.getUnfulfilledQuota() + change;
            otherFee.setUnfulfilledQuota(quota);
            otherFee.setExecuteRate(ComputeUtils.computeExecuteRate(otherFee.getSubTotal(), quota));

            // 更新项目金额信息
            planService.updatePlanFunding(otherFee.getPlanId(), change);
        }
        otherFeeMapper.update(otherFee);
    }

    public OtherFee getById(Long id) {
        return otherFeeMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        otherFeeMapper.clear(user.getUsername());
    }

    public List<OtherFee> getRecommendFee() {
        // 查询所有设备费信息
        List<OtherFee> otherFeeList = otherFeeMapper.getAll();
        if (CollectionUtils.isEmpty(otherFeeList)) {
            return new ArrayList<>();
        }
        // 获取最近一条设备费执行记录
        Long id = budgetRecordMapper.getRecentIdByItemType(PlanItemTypeEnum.OTHERS.getIndex(), UserInfoUtils.getLoginedUser().getUsername());
        if (id == null) {
            return otherFeeList.size() > 10 ? otherFeeList.subList(0, 10) : otherFeeList;
        }
        OtherFee fee = otherFeeMapper.getById(id);
        if (fee == null) {
            return otherFeeList.size() > 10 ? otherFeeList.subList(0, 10) : otherFeeList;
        }
        // 计算相似度，并按照相似度逆序排列
        for (OtherFee e : otherFeeList) {
            e.computeSimilar(fee);
        }
        Collections.sort(otherFeeList, Comparator.comparingDouble(x -> -x.getSimilar()));
        return otherFeeList.size() > 10 ? otherFeeList.subList(0, 10) : otherFeeList;
    }
}
