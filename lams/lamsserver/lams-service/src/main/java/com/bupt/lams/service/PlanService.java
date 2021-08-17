package com.bupt.lams.service;

import com.bupt.lams.constants.BudgetConstant;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.dto.PlanQueryCondition;
import com.bupt.lams.mapper.PlanMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Plan;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.utils.SpringContextUtil;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 预算计划service
 */
@Service
public class PlanService {
    @Resource
    PlanMapper planMapper;

    @Resource
    PlanAndPlanItemService planAndPlanItemService;

    public RespPageBean getPlanByCondition(PlanQueryCondition condition) {
        try {
            Integer page = condition.getPage();
            Integer size = condition.getSize();
            if (page != null && size != null) {
                page = (page - 1) * size;
            }
            List<Plan> data = getFullPlanInfo(condition);
            return transToRespPageBean(page, size, data);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Plan> getFullPlanInfo(PlanQueryCondition condition) throws Exception {
        List<Plan> data = planMapper.getPlanByCondition(condition);
        data = buildPlanInfo(data);
        return data;
    }

    public Plan getFullPlanInfoById(Long id) {
        PlanQueryCondition condition = new PlanQueryCondition();
        condition.setPlanId(id);
        try {
            List<Plan> planList = getFullPlanInfo(condition);
            if (CollectionUtils.isEmpty(planList)) {
                return null;
            }
            return planList.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addPlan(Plan plan) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        plan.setUserEmail(user.getUsername());
        plan.setUnfulfilledQuota(plan.getFunding());
        plan.setExecuteRate(0.0);
        planMapper.insertSelective(plan);
        planAndPlanItemService.addPlanAndItemRelationByPlan(plan);
        return plan.getId();
    }

    public void updatePlan(Plan plan) {
        planMapper.updatePlan(plan);
    }

    public List<Plan> getPlansByProjectIds(List<Long> pids) {
        return planMapper.getPlansByProjectIds(pids);
    }

    /**
     * 将结果集转化为展示集合
     *
     * @param page
     * @param size
     * @param data
     * @return
     */
    private RespPageBean transToRespPageBean(Integer page, Integer size, List<Plan> data) {
        Long total = Long.valueOf(data.size());
        Integer end = Math.min(page + size, data.size());
        // 展示的数据
        List<Plan> res = new ArrayList<>();
        for (Integer i = page; i < end; i++) {
            res.add(data.get(i));
        }
        RespPageBean bean = new RespPageBean();
        bean.setData(res);
        bean.setTotal(total);
        return bean;
    }

    private List<Plan> buildPlanInfo(List<Plan> planList) throws Exception {
        if (planList == null && planList.size() == 0) {
            return planList;
        }
        for (Plan plan : planList) {
            List<PlanAndItem> planAndItemList = planAndPlanItemService.getPlanAndItemListByPid(plan.getId());
            for (Map.Entry entry : BudgetConstant.PlanItemMap.entrySet()) {
                Class clz = (Class) entry.getValue();
                Method getRecord = clz.getMethod("getDataByPlanAndItem", List.class);
                PlanItemData planItemData = (PlanItemData) getRecord.invoke(SpringContextUtil.getBean((Class) entry.getValue()), planAndItemList);
                plan.setPlanItem((String) entry.getKey(), planItemData);
            }
        }
        return planList;
    }

    public Plan getPlanById(Long id) {
        return planMapper.getPlanById(id);
    }

    public List<Plan> getAllPlans() {
        return planMapper.getAllPlans();
    }
}
