package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.mapper.EquipmentFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.EquipmentFee;
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
 * 设备费service
 */
@Service
public class EquipmentFeeService {
    @Resource
    EquipmentFeeMapper equipmentFeeMapper;
    @Resource
    BudgetRecordMapper budgetRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addEquipmentFee(EquipmentFee equipmentFee) {
        equipmentFee.setItemType(PlanItemTypeEnum.EQUIPMENT.getIndex());
        equipmentFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
        // 计算合计
        double subTotal = equipmentFee.getUnitPrice() * equipmentFee.getAmount();
        equipmentFee.setSubTotal(subTotal);
        equipmentFee.setUnfulfilledQuota(subTotal);
        equipmentFeeMapper.insertSelective(equipmentFee);
        return equipmentFee.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.EQUIPMENT.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        List<EquipmentFee> equipmentFeeList = equipmentFeeMapper.getFullInfoByIds(idList);
        data = TransferUtils.transToPlanItemData(new ArrayList<>(equipmentFeeList));
        return data;
    }

    public PlanItemData getEquipmentFeeDataByIds(List<Long> ids) {
        List<EquipmentFee> equipmentFeeList = equipmentFeeMapper.getEquipmentFeeByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(equipmentFeeList));
    }

    public void update(EquipmentFee equipmentFee) {
        equipmentFeeMapper.update(equipmentFee);
    }

    public EquipmentFee getById(Long id) {
        return equipmentFeeMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        equipmentFeeMapper.clear(user.getUsername());
    }


    public List<EquipmentFee> getRecommendFee() {
        // 查询所有设备费信息
        List<EquipmentFee> equipmentFeeList = equipmentFeeMapper.getAll();
        if (CollectionUtils.isEmpty(equipmentFeeList)) {
            return new ArrayList<>();
        }
        // 获取最近一条设备费执行记录
        Long id = budgetRecordMapper.getRecentIdByItemType(PlanItemTypeEnum.EQUIPMENT.getIndex());
        if (id == null) {
            return equipmentFeeList.size() > 10 ? equipmentFeeList.subList(0, 10) : equipmentFeeList;
        }
        EquipmentFee fee = equipmentFeeMapper.getById(id);
        if (fee == null) {
            return equipmentFeeList.size() > 10 ? equipmentFeeList.subList(0, 10) : equipmentFeeList;
        }
        // 计算相似度，并按照相似度逆序排列
        for (EquipmentFee e : equipmentFeeList) {
            e.computeSimilar(fee);
        }
        Collections.sort(equipmentFeeList, Comparator.comparingDouble(x -> -x.getSimilar()));
        return equipmentFeeList.size() > 10 ? equipmentFeeList.subList(0, 10) : equipmentFeeList;
    }
}
