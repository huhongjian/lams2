package com.bupt.lams.service;

import com.bupt.lams.constants.BudgetConstant;
import com.bupt.lams.constants.BudgetReimburseEnum;
import com.bupt.lams.constants.ConfigStatusEnum;
import com.bupt.lams.dto.ExecutionQueryCondition;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.mapper.PlanMapper;
import com.bupt.lams.mapper.ProjectMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.model.planItem.PlanItem;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.SpringContextUtil;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
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
    PlanMapper planMapper;
    @Resource
    ProjectMapper projectMapper;

    public RespPageBean getBudgetRecordByCondition(ExecutionQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<BudgetRecord> data = budgetRecordMapper.getBudgetRecordByCondition(condition);
        return transToRespPageBean(page, size, data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addBudgetRecord(BudgetRecord budgetRecord) throws Exception {
        LamsUser user = UserInfoUtils.getLoginedUser();
        budgetRecord.setUserEmail(user.getUsername());
        budgetRecordMapper.insertSelective(budgetRecord);
        executePlanItem(budgetRecord, false);
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

    private void executePlanItem(BudgetRecord budgetRecord, Boolean isRevert) throws Exception {
        Double funding = budgetRecord.getFunding();
        if (isRevert) {
            funding = -funding;
        }

        Class service = BudgetConstant.ItemTypeMap.get(budgetRecord.getItemType());
        Class pojo = BudgetConstant.ItemTypePojoMap.get(budgetRecord.getItemType());
        Method getById = service.getMethod("getById", Long.class);
        Object data = getById.invoke(SpringContextUtil.getBean(service), budgetRecord.getPlanItemId());
        PlanItem planItemData = (PlanItem) data;
        Double unfulfilledQuota = planItemData.getUnfulfilledQuota();
        if (unfulfilledQuota < funding) {
            logger.error("[executePlanItem]:执行金额过大！");
            throw new Exception("执行金额过大！");
        }

        Method setUnfulfilledQuota = pojo.getMethod("setUnfulfilledQuota", Double.class);
        setUnfulfilledQuota.invoke(data, unfulfilledQuota - funding);
        Method setExecuteRate = pojo.getMethod("setExecuteRate", Double.class);
        setExecuteRate.invoke(data, ComputeUtils.computeExecuteRate(planItemData.getSubTotal(), unfulfilledQuota - funding));
        Method update = service.getMethod("update", pojo);
        update.invoke(SpringContextUtil.getBean(service), data);

        // 更新预算计划信息
        Plan plan = planMapper.getPlanById(budgetRecord.getPlanId());
        if (plan.getUnfulfilledQuota() < funding) {
            logger.error("[executePlanItem]:执行金额过大！");
            throw new Exception("执行金额过大！");
        }
        plan.setUnfulfilledQuota(plan.getUnfulfilledQuota() - funding);
        plan.setExecuteRate(ComputeUtils.computeExecuteRate(plan.getFunding(), plan.getUnfulfilledQuota()));
        planMapper.updatePlan(plan);
        // 更新科研项目信息
        Project project = projectMapper.getProjectById(budgetRecord.getProjectId());
        if (project.getUnfulfilledQuota() < funding) {
            logger.error("[executePlanItem]:执行金额过大！");
            throw new Exception("执行金额过大！");
        }
        project.setUnfulfilledQuota(project.getUnfulfilledQuota() - funding);
        project.setExecuteRate(ComputeUtils.computeExecuteRate(project.getFunding(), project.getUnfulfilledQuota()));
        projectMapper.updateProject(project);
    }

    public List<BudgetRecord> getBudgetRecordByPlanItem(PlanItem item) {
        ExecutionQueryCondition condition = new ExecutionQueryCondition();
        condition.setItemType(item.getItemType());
        condition.setPlanItemId(item.getId());
        return budgetRecordMapper.getBudgetRecordByCondition(condition);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteExecution(List<Long> rids) throws Exception {
        List<BudgetRecord> recordList = budgetRecordMapper.getByIds(rids);
        if (CollectionUtils.isEmpty(recordList)) {
            return;
        }
        for (BudgetRecord record : recordList) {
            executePlanItem(record, true);
        }
        budgetRecordMapper.deleteManyByIds(rids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reimburseRecord(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (Long id : ids) {
            BudgetRecord budgetRecord = new BudgetRecord();
            budgetRecord.setId(id);
            budgetRecord.setReimburse(BudgetReimburseEnum.YES.getIndex());
            budgetRecordMapper.update(budgetRecord);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelReimburse(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (Long id : ids) {
            BudgetRecord budgetRecord = new BudgetRecord();
            budgetRecord.setId(id);
            budgetRecord.setReimburse(BudgetReimburseEnum.NO.getIndex());
            budgetRecordMapper.update(budgetRecord);
        }
    }
}
