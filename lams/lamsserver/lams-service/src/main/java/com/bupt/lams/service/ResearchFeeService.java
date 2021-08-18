package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.ResearchFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.PublishFee;
import com.bupt.lams.model.planItem.ResearchFee;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 课题调研差旅费service
 */
@Service
public class ResearchFeeService {
    @Resource
    ResearchFeeMapper researchFeeMapper;

    public List<ResearchFee> getResearchFeeList() {
        return researchFeeMapper.getResearchFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addResearchFee(ResearchFee researchFee) {
        researchFee.setItemType(PlanItemTypeEnum.RESEARCH.getIndex());
        // 计算合计
        Double total = (researchFee.getAccommodationFee() + researchFee.getOtherFee() + researchFee.getTransFee()) * researchFee.getDays() * researchFee.getTimes() / 10000D;
        researchFee.setSubTotal(total);
        researchFee.setUnfulfilledQuota(researchFee.getSubTotal());
        researchFee.setExecuteRate(ComputeUtils.computeExecuteRate(researchFee.getSubTotal(), researchFee.getUnfulfilledQuota()));
        researchFeeMapper.insertSelective(researchFee);
        return researchFee.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.RESEARCH.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        data = getResearchFeeDataByIds(idList);
        return data;
    }

    public PlanItemData getResearchFeeDataByIds(List<Long> ids) {
        List<ResearchFee> researchFeeList = researchFeeMapper.getResearchFeeByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(researchFeeList));
    }

    public List<ResearchFee> getResearchFeeListByIds(List<Long> pids) {
        return researchFeeMapper.getResearchFeeByIds(pids);
    }
}
