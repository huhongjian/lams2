package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.EquipmentFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.Advisory;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.model.planItem.EquipmentFeeData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备费service
 */
@Service
public class EquipmentFeeService {
    @Resource
    EquipmentFeeMapper equipmentFeeMapper;

    public List<EquipmentFee> getEquipmentFeeList() {
        return equipmentFeeMapper.getEquipmentFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addEquipmentFee(EquipmentFee equipmentFee) {
        // 计算合计
        equipmentFee.setSubTotal(equipmentFee.getUnitPrice() * equipmentFee.getAmount());
        equipmentFeeMapper.insertSelective(equipmentFee);
        return equipmentFee.getId();
    }

    public EquipmentFeeData getEquipmentFeeByPlanAndItem(List<PlanAndItem> planAndItemList) {
        EquipmentFeeData data = new EquipmentFeeData();
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
        Double total = new Double(0.0);
        Double unfulfilledQuota = new Double(0.0);
        DecimalFormat df = new DecimalFormat("#.00");
        List<EquipmentFee> equipmentFeeList = equipmentFeeMapper.getEquipmentFeeByIds(idList);
        for (EquipmentFee fee : equipmentFeeList) {
            total = total + fee.getSubTotal();
            unfulfilledQuota = unfulfilledQuota + fee.getUnfulfilledQuota();
        }
        data.setFunding(total);
        data.setUnfulfilledQuota(unfulfilledQuota);
        Double executeRate = (total - unfulfilledQuota) / total * 100;
        data.setExecuteRate(new Double(df.format(executeRate.doubleValue())));
        data.setEquipmentFeeList(equipmentFeeList);
        return data;
    }

    public List<EquipmentFee> getEquipmentFeeListByIds(List<Long> pids) {
        return equipmentFeeMapper.getEquipmentFeeByIds(pids);
    }

    public void update(EquipmentFee equipmentFee) {
        equipmentFeeMapper.update(equipmentFee);
    }

    public EquipmentFee getEquipmentFeeById(Long id) {
        return equipmentFeeMapper.getEquipmentFeeById(id);
    }
}
