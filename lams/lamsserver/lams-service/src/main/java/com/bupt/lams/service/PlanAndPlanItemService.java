package com.bupt.lams.service;

import com.bupt.lams.mapper.PlanAndItemMapper;
import com.bupt.lams.model.Plan;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.PlanItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PlanAndPlanItemService {
    @Resource
    PlanAndItemMapper planAndItemMapper;

    public List<PlanAndItem> getPlanAndItemListByPid(Long pid) {
        return planAndItemMapper.getPlanAndItemListByPid(pid);
    }

    /**
     * 更新工单和资产之前的关联关系
     * 删除旧绑定关系，添加新关系
     *
     * @param plan
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPlanAndItemRelationByPlan(Plan plan) {
        List<PlanAndItem> planAndItemList = new ArrayList<>();
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getAdvisoryList() == null ? null : new ArrayList<>(plan.getAdvisoryList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getAcademicFeeList() == null ? null : new ArrayList<>(plan.getAcademicFeeList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getEquipmentFeeData() == null || plan.getEquipmentFeeData().getEquipmentFeeList() == null ? null : new ArrayList<>(plan.getEquipmentFeeData().getEquipmentFeeList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getMaterialFeeList() == null ? null : new ArrayList<>(plan.getMaterialFeeList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getMeetingFeeList() == null ? null : new ArrayList<>(plan.getMeetingFeeList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getOtherFeeList() == null ? null : new ArrayList<>(plan.getOtherFeeList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getPaymentList() == null ? null : new ArrayList<>(plan.getPaymentList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getPublishFeeList() == null ? null : new ArrayList<>(plan.getPublishFeeList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getResearchFeeList() == null ? null : new ArrayList<>(plan.getResearchFeeList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getTransFeeList() == null ? null : new ArrayList<>(plan.getTransFeeList()), planAndItemList);
        planAndItemList = addPlanAndItemRelation(plan.getId(), plan.getTestingFeeList() == null ? null : new ArrayList<>(plan.getTestingFeeList()), planAndItemList);
        if (planAndItemList.size() == 0) {
            return;
        }
        planAndItemMapper.insertMany(planAndItemList);
    }

    public List<PlanAndItem> addPlanAndItemRelation(Long pid, List<PlanItem> planItemList, List<PlanAndItem> planAndItemList) {
        if (planItemList == null || planItemList.size() == 0) {
            return planAndItemList;
        }
        for (PlanItem item : planItemList) {
            PlanAndItem planAndItem = new PlanAndItem(pid, item.getItemType(), item.getId(), new Date(), new Date());
            planAndItemList.add(planAndItem);
        }
        return planAndItemList;
    }
}
