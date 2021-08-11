package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.MaterialFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.model.planItem.Advisory;
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

    public List<MaterialFee> getMaterialFeeByPlanAndItem(List<PlanAndItem> planAndItemList) {
        List<MaterialFee> res = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return res;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.MATERIAL.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return res;
        }
        return materialFeeMapper.getMaterialFeeByIds(idList);
    }

    public List<MaterialFee> getMaterialFeeListByIds(List<Long> pids) {
        return materialFeeMapper.getMaterialFeeByIds(pids);
    }
}
