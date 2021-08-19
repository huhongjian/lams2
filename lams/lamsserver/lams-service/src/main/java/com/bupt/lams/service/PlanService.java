package com.bupt.lams.service;

import com.bupt.lams.constants.BudgetConstant;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.dto.PlanQueryCondition;
import com.bupt.lams.mapper.PlanMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.PlanItem;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.SpringContextUtil;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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
    EquipmentFeeService equipmentFeeService;

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

    @Transactional(rollbackFor = Exception.class)
    public void updatePlan(Plan plan) {
        Plan oldPlan = planMapper.getPlanById(plan.getId());
        // 如果总额有更新, 则更新相关数据
        if (oldPlan.getFunding() != plan.getFunding()) {
            Double change = plan.getFunding() - oldPlan.getFunding();
            double quota = plan.getUnfulfilledQuota() + change;
            plan.setUnfulfilledQuota(quota);
            plan.setExecuteRate(ComputeUtils.computeExecuteRate(plan.getFunding(), quota));
        }
        planMapper.updatePlan(plan);
        // 更新和预算明细的关联关系
        planAndPlanItemService.updatePlanAndItemRelationByPlan(plan);
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

    public void deletePlans(List<Long> ids) {
        planMapper.deletePlans(ids);
    }

    /**
     * excel转预算计划信息
     */
    public Plan excel2Plan(MultipartFile file) {
        Plan plan = new Plan();
        Double total = 0.0D;
        Map<String, PlanItemData> planItemDataMap = new HashMap<>();

        try {
            // 创建一个 workbook 对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());

            // 填充预算计划信息
            plan = fillPlanInfo(workbook, plan);

            // 获取设备费表单
            PlanItemData equipmentFeeData = getEquipmentFeeData(workbook);
            planItemDataMap.put("equipmentFeeData", equipmentFeeData);
            total += equipmentFeeData.getFunding();

            plan.setFunding(total);
            plan.setPlanItemDataMap(planItemDataMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return plan;
    }

    private Plan fillPlanInfo(HSSFWorkbook workbook, Plan plan) {
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 获取行
        HSSFRow row = sheet.getRow(1);
        // 获取列数
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        for (int k = 0; k < physicalNumberOfCells; k++) {
            HSSFCell cell = row.getCell(k);
            switch (cell.getCellType()) {
                case STRING:
                    String cellValue = cell.getStringCellValue();
                    switch (k) {
                        case 1:
                            plan.setPlanName(cellValue);
                            break;
                        case 3:
                            plan.setProjectName(cellValue);
                            break;
                        case 4:
                            plan.setRemark(cellValue);
                            break;
                    }
                    break;
                default: {
                    switch (k) {
                        case 2:
                            plan.setProjectId((long) cell.getNumericCellValue());
                            break;
                    }
                }
                break;
            }
        }
        return plan;
    }

    private PlanItemData getEquipmentFeeData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        EquipmentFee equipmentFee = null;
        HSSFSheet sheet = workbook.getSheetAt(1);
        // 获取表单中的行数
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        for (int j = 0; j < physicalNumberOfRows; j++) {
            // 跳过标题行
            if (j == 0) {
                continue;//跳过标题行
            }
            //6. 获取行
            HSSFRow row = sheet.getRow(j);
            if (row == null) {
                continue;//防止数据中间有空行
            }
            //7. 获取列数
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();
            equipmentFee = new EquipmentFee();
            for (int k = 0; k < physicalNumberOfCells; k++) {
                HSSFCell cell = row.getCell(k);
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 1:
                                equipmentFee.setEquipmentName(cellValue);
                                break;
                            case 2:
                                equipmentFee.setUsage(cellValue);
                                break;
                            case 5:
                                equipmentFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 3:
                                equipmentFee.setAmount((int) cell.getNumericCellValue());
                                break;
                            case 4:
                                equipmentFee.setUnitPrice(cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            equipmentFeeService.addEquipmentFee(equipmentFee);
            list.add(equipmentFee);
        }
        data.setPlanItemList(list);
        return data;
    }
}
