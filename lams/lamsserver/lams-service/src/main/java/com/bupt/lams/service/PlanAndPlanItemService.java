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

    /**
     * 更新工单和资产之前的关联关系
     * 删除旧绑定关系，添加新关系
     *
     * @param plan
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPlanAndItemRelationByPlan(Plan plan) {
        List<PlanAndItem> planAndItemList = buildPlanAndItemRelation(plan);
        if (CollectionUtils.isEmpty(planAndItemList)) {
            return;
        }
        planAndItemMapper.insertMany(planAndItemList);
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
