package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.EquipmentFeeMapper;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespPageBean;
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

    public List<EquipmentFee> getEquipmentFeeList() {
        return equipmentFeeMapper.getEquipmentFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addEquipmentFee(EquipmentFee equipmentFee) {
        equipmentFee.setItemType(PlanItemTypeEnum.EQUIPMENT.getIndex());
        // 计算合计
        equipmentFee.setSubTotal(equipmentFee.getUnitPrice() * equipmentFee.getAmount());
        equipmentFeeMapper.insertSelective(equipmentFee);
        return equipmentFee.getId();
    }

    public void updateAsset(Project project) {
        equipmentFeeMapper.updateProject(project);
    }
}
