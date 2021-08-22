package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.mapper.ResearchFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.ResearchFee;
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
 * 课题调研差旅费service
 */
@Service
public class ResearchFeeService {
    @Resource
    ResearchFeeMapper researchFeeMapper;
    @Resource
    BudgetRecordMapper budgetRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addResearchFee(ResearchFee researchFee) {
        researchFee.setItemType(PlanItemTypeEnum.RESEARCH.getIndex());
        researchFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
        // 计算合计
        Double total = (researchFee.getTransFee() * researchFee.getTimes() + (researchFee.getAccommodationFee() + researchFee.getOtherFee()) * researchFee.getDays() * researchFee.getTimes()) / 10000D;
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
        List<ResearchFee> researchFeeList = researchFeeMapper.getFullInfoByIds(idList);
        data = TransferUtils.transToPlanItemData(new ArrayList<>(researchFeeList));
        return data;
    }

    public PlanItemData getResearchFeeDataByIds(List<Long> ids) {
        List<ResearchFee> researchFeeList = researchFeeMapper.getResearchFeeByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(researchFeeList));
    }

    public List<ResearchFee> getResearchFeeListByIds(List<Long> pids) {
        return researchFeeMapper.getResearchFeeByIds(pids);
    }

    public void update(ResearchFee equipmentFee) {
        researchFeeMapper.update(equipmentFee);
    }

    public ResearchFee getById(Long id) {
        return researchFeeMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        researchFeeMapper.clear(user.getUsername());
    }

    public List<ResearchFee> getRecommendFee() {
        // 查询所有设备费信息
        List<ResearchFee> researchFeeList = researchFeeMapper.getAll();
        if (CollectionUtils.isEmpty(researchFeeList)) {
            return new ArrayList<>();
        }
        // 获取最近一条设备费执行记录
        Long id = budgetRecordMapper.getRecentIdByItemType(PlanItemTypeEnum.RESEARCH.getIndex());
        if (id == null) {
            return researchFeeList.size() > 10 ? researchFeeList.subList(0, 10) : researchFeeList;
        }
        ResearchFee fee = researchFeeMapper.getById(id);
        if (fee == null) {
            return researchFeeList.size() > 10 ? researchFeeList.subList(0, 10) : researchFeeList;
        }
        // 计算相似度，并按照相似度逆序排列
        for (ResearchFee e : researchFeeList) {
            e.computeSimilar(fee);
        }
        Collections.sort(researchFeeList, Comparator.comparingDouble(x -> -x.getSimilar()));
        return researchFeeList.size() > 10 ? researchFeeList.subList(0, 10) : researchFeeList;
    }
}
