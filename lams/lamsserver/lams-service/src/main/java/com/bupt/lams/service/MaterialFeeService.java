package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.MaterialFeeMapper;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.MaterialFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 材料费service
 */
@Service
public class MaterialFeeService {
    @Resource
    MaterialFeeMapper materialFeeMapper;

    public List<MaterialFee> getMaterialFeeList() {
        return materialFeeMapper.getMaterialFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addMaterialFee(MaterialFee materialFee) {
        materialFee.setItemType(PlanItemTypeEnum.MATERIAL.getIndex());
        // 计算合计
        if (materialFee.getSubTotal() == null) {
            materialFee.setSubTotal(materialFee.getUnitPrice() * materialFee.getAmount());
        }
        materialFeeMapper.insertSelective(materialFee);
        return materialFee.getId();
    }

    public void updateAsset(Project project) {
        materialFeeMapper.updateProject(project);
    }
}
