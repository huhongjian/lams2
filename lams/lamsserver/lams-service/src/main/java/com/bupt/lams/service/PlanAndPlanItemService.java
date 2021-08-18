package com.bupt.lams.service;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.PlanAndItemMapper;
import com.bupt.lams.model.Plan;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.PlanItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PlanAndPlanItemService {
    @Resource
    PlanAndItemMapper planAndItemMapper;

    public List<PlanAndItem> getPlanAndItemListByPid(Long pid) {
        return planAndItemMapper.getPlanAndItemListByPid(pid);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addPlanAndItemRelationByPlan(Plan plan) {
        List<PlanAndItem> planAndItemList = buildPlanAndItemRelation(plan);
        if (CollectionUtils.isEmpty(planAndItemList)) {
            return;
        }
        planAndItemMapper.insertMany(planAndItemList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePlanAndItemRelationByPlan(Plan plan) {
        // 删除旧的
        List<Long> pids = new ArrayList<>();
        pids.add(plan.getId());
        planAndItemMapper.deleteManyByPids(pids);
        // 插入新的
        addPlanAndItemRelationByPlan(plan);
    }


    public List<PlanAndItem> buildPlanAndItemRelation(Plan plan) {
        List<PlanAndItem> planAndItemList = new ArrayList<>();
        Map<String, PlanItemData> planItemDataMap = plan.getPlanItemDataMap();
        for (PlanItemData data : planItemDataMap.values()) {
            if (CollectionUtils.isEmpty(data.getPlanItemList())) {
                continue;
            }
            for (PlanItem item : data.getPlanItemList()) {
                PlanAndItem planAndItem = new PlanAndItem(plan.getId(), item.getItemType(), item.getId(), new Date(), new Date());
                planAndItemList.add(planAndItem);
            }
        }
        return planAndItemList;
    }
}
