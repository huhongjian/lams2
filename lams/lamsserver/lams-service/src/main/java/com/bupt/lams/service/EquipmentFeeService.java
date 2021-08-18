package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.EquipmentFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.utils.TransferUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备费service
 */
@Service
public class EquipmentFeeService {
    @Resource
    EquipmentFeeMapper equipmentFeeMapper;

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
        data = getEquipmentFeeDataByIds(idList);
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
}
