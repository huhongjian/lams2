package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.mapper.MaterialFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.MaterialFee;
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
 * 材料费service
 */
@Service
public class MaterialFeeService {
    @Resource
    MaterialFeeMapper materialFeeMapper;
    @Resource
    BudgetRecordMapper budgetRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addMaterialFee(MaterialFee materialFee) {
        materialFee.setItemType(PlanItemTypeEnum.MATERIAL.getIndex());
        materialFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
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
        List<MaterialFee> materialFeeList = materialFeeMapper.getFullInfoByIds(idList);
        data = TransferUtils.transToPlanItemData(new ArrayList<>(materialFeeList));
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

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        materialFeeMapper.clear(user.getUsername());
    }

    public List<MaterialFee> getRecommendFee() {
        // 查询所有设备费信息
        List<MaterialFee> materialFeeList = materialFeeMapper.getAll();
        if (CollectionUtils.isEmpty(materialFeeList)) {
            return new ArrayList<>();
        }
        // 获取最近一条设备费执行记录
        Long id = budgetRecordMapper.getRecentIdByItemType(PlanItemTypeEnum.MATERIAL.getIndex());
        if (id == null) {
            return materialFeeList;
        }
        MaterialFee fee = materialFeeMapper.getById(id);
        if (fee == null) {
            return materialFeeList;
        }
        // 计算相似度，并按照相似度逆序排列
        for (MaterialFee e : materialFeeList) {
            e.computeSimilar(fee);
        }
        Collections.sort(materialFeeList, Comparator.comparingDouble(x -> -x.getSimilar()));
        return materialFeeList;
    }
}
