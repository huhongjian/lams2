package com.bupt.lams.service;

import com.bupt.lams.constants.PlanStatusEnum;
import com.bupt.lams.dto.PlanQueryCondition;
import com.bupt.lams.mapper.PlanMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 预算计划service
 */
@Service
public class PlanService {
    @Resource
    PlanMapper planMapper;

    @Resource
    PlanAndPlanItemService planAndPlanItemService;

    @Resource
    AcademicFeeService academicFeeService;
    @Resource
    AdvisoryService advisoryService;
    @Resource
    EquipmentFeeService equipmentFeeService;
    @Resource
    MaterialFeeService materialFeeService;
    @Resource
    MeetingFeeService meetingFeeService;
    @Resource
    OtherFeeService otherFeeService;
    @Resource
    PaymentService paymentService;
    @Resource
    TransportationFeeService transportationFeeService;
    @Resource
    PublishFeeService publishFeeService;
    @Resource
    ResearchFeeService researchFeeService;
    @Resource
    TestingFeeService testingFeeService;

    public RespPageBean getPlanByCondition(PlanQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Plan> data = getFullPlanInfo(condition);
        return transToRespPageBean(page, size, data);
    }

    public List<Plan> getFullPlanInfo(PlanQueryCondition condition) {
        List<Plan> data = planMapper.getPlanByCondition(condition);
        data = buildPlanInfo(data);
        return data;
    }

    public Plan getFullPlanInfoById(Long id) {
        PlanQueryCondition condition = new PlanQueryCondition();
        condition.setPlanId(id);
        List<Plan> planList = getFullPlanInfo(condition);
        if (CollectionUtils.isEmpty(planList)) {
            return null;
        }
        return planList.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addPlan(Plan plan) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        plan.setUserEmail(user.getUsername());
        plan.setStatus(PlanStatusEnum.ON.getIndex());
        plan.setUnfulfilledQuota(plan.getFunding());
        plan.setExecuteRate(0.0);
        planMapper.insertSelective(plan);
        planAndPlanItemService.addPlanAndItemRelationByPlan(plan);
        return plan.getId();
    }

    public void updatePlan(Plan plan) {
        planMapper.updatePlan(plan);
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

    private List<Plan> buildPlanInfo(List<Plan> planList) {
        if (planList == null && planList.size() == 0) {
            return planList;
        }
        for (Plan plan : planList) {
            List<PlanAndItem> planAndItemList = planAndPlanItemService.getPlanAndItemListByPid(plan.getId());
            plan.setAcademicFeeList(academicFeeService.getAcademicFeeByPlanAndItem(planAndItemList));
            plan.setAdvisoryList(advisoryService.getAdvisoryByPlanAndItem(planAndItemList));
            plan.setEquipmentFeeData(equipmentFeeService.getEquipmentFeeByPlanAndItem(planAndItemList));
            plan.setMaterialFeeList(materialFeeService.getMaterialFeeByPlanAndItem(planAndItemList));
            plan.setMeetingFeeList(meetingFeeService.getMeetingFeeByPlanAndItem(planAndItemList));
            plan.setOtherFeeList(otherFeeService.getOtherFeeByPlanAndItem(planAndItemList));
            plan.setPaymentList(paymentService.getPaymentByPlanAndItem(planAndItemList));
            plan.setTransFeeList(transportationFeeService.getTransportationFeeByPlanAndItem(planAndItemList));
            plan.setPublishFeeList(publishFeeService.getPublishFeeByPlanAndItem(planAndItemList));
            plan.setResearchFeeList(researchFeeService.getResearchFeeByPlanAndItem(planAndItemList));
            plan.setTestingFeeList(testingFeeService.getTestingFeeByPlanAndItem(planAndItemList));
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
