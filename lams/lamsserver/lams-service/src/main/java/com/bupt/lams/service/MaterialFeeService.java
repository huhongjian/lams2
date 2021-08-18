package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.MaterialFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.MaterialFee;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
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
        materialFee.setUnfulfilledQuota(materialFee.getSubTotal());
        materialFee.setExecuteRate(ComputeUtils.computeExecuteRate(materialFee.getSubTotal(), materialFee.getUnfulfilledQuota()));
        materialFeeMapper.insertSelective(materialFee);
        return materialFee.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.MATERIAL.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        data = getMaterialFeeDataByIds(idList);
        return data;
    }

    public PlanItemData getMaterialFeeDataByIds(List<Long> ids) {
        List<MaterialFee> materialFeeList = materialFeeMapper.getMaterialFeeByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(materialFeeList));
    }

    public List<MaterialFee> getMaterialFeeListByIds(List<Long> pids) {
        return materialFeeMapper.getMaterialFeeByIds(pids);
    }

    public void update(MaterialFee materialFee) {
        materialFeeMapper.update(materialFee);
    }

    public MaterialFee getById(Long id) {
        return materialFeeMapper.getById(id);
    }
}
