package com.bupt.lams.service;

import com.bupt.lams.dto.ExecutionQueryCondition;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目service
 */
@Service
public class BudgetRecordService {
    private Logger logger = LoggerFactory.getLogger(BudgetRecordService.class);

    @Resource
    BudgetRecordMapper budgetRecordMapper;

    @Resource
    PlanService planService;
    @Resource
    ProjectService projectService;

    @Resource
    EquipmentFeeService equipmentFeeService;

    public RespPageBean getBudgetRecordByCondition(ExecutionQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<BudgetRecord> data = budgetRecordMapper.getBudgetRecordByCondition(condition);
        return transToRespPageBean(page, size, data);
    }

    public List<Project> getAllProjects() {
        return budgetRecordMapper.getAllProjects();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addBudgetRecord(BudgetRecord budgetRecord) throws Exception {
        LamsUser user = UserInfoUtils.getLoginedUser();
        budgetRecord.setUserEmail(user.getUsername());
        budgetRecordMapper.insertSelective(budgetRecord);
        executePlanItem(budgetRecord);
        return budgetRecord.getId();
    }

    /**
     * 将结果集转化为展示集合
     *
     * @param page
     * @param size
     * @param data
     * @return
     */
    private RespPageBean transToRespPageBean(Integer page, Integer size, List<BudgetRecord> data) {
        Long total = Long.valueOf(data.size());
        Integer end = Math.min(page + size, data.size());
        // 展示的数据
        List<BudgetRecord> res = new ArrayList<>();
        for (Integer i = page; i < end; i++) {
            res.add(data.get(i));
        }
        RespPageBean bean = new RespPageBean();
        bean.setData(res);
        bean.setTotal(total);
        return bean;
    }

    private void executePlanItem(BudgetRecord budgetRecord) throws Exception {
        DecimalFormat df = new DecimalFormat("#.00");
        Double funding = budgetRecord.getFunding();
        Double executeRate;
        switch (budgetRecord.getItemType()) {
            case 1:
                EquipmentFee equipmentFee = equipmentFeeService.getEquipmentFeeById(budgetRecord.getPlanItemId());
                if (equipmentFee.getUnfulfilledQuota() < funding) {
                    logger.error("[executePlanItem]:执行金额过大！");
                    throw new Exception("[executePlanItem]:执行金额过大！");
                }
                equipmentFee.setUnfulfilledQuota(equipmentFee.getUnfulfilledQuota() - funding);
                executeRate = (equipmentFee.getSubTotal() - equipmentFee.getUnfulfilledQuota()) / equipmentFee.getSubTotal() * 100;
                equipmentFee.setExecuteRate(new Double(df.format(executeRate.doubleValue())));
                equipmentFeeService.update(equipmentFee);
                break;
        }
        // 更新预算计划信息
        Plan plan = planService.getPlanById(budgetRecord.getPlanId());
        if (plan.getUnfulfilledQuota() < funding) {
            logger.error("[executePlanItem]:执行金额过大！");
            throw new Exception("[executePlanItem]:执行金额过大！");
        }
        plan.setUnfulfilledQuota(plan.getUnfulfilledQuota() - funding);
        executeRate = (plan.getFunding() - plan.getUnfulfilledQuota()) / plan.getFunding() * 100;
        plan.setExecuteRate(new Double(df.format(executeRate.doubleValue())));
        planService.updatePlan(plan);
        // 更新科研项目信息
        Project project = projectService.getProjectById(budgetRecord.getProjectId());
        if (project.getUnfulfilledQuota() < funding) {
            logger.error("[executePlanItem]:执行金额过大！");
            throw new Exception("[executePlanItem]:执行金额过大！");
        }
        project.setUnfulfilledQuota(project.getUnfulfilledQuota() - funding);
        executeRate = (project.getFunding() - project.getUnfulfilledQuota()) / project.getFunding() * 100;
        project.setExecuteRate(new Double(df.format(executeRate.doubleValue())));
        projectService.updateProject(project);
    }
}
