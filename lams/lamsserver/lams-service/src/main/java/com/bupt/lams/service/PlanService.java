package com.bupt.lams.service;

import com.bupt.lams.constants.BudgetConstant;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.dto.PlanQueryCondition;
import com.bupt.lams.mapper.PlanMapper;
import com.bupt.lams.mapper.ProjectMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.model.planItem.*;
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
    ProjectMapper projectMapper;

    @Resource
    EquipmentFeeService equipmentFeeService;
    @Resource
    MaterialFeeService materialFeeService;
    @Resource
    TestingFeeService testingFeeService;
    @Resource
    MeetingFeeService meetingFeeService;
    @Resource
    ResearchFeeService researchFeeService;
    @Resource
    AcademicFeeService academicFeeService;
    @Resource
    TransportationFeeService transportationFeeService;
    @Resource
    PublishFeeService publishFeeService;
    @Resource
    PaymentService paymentService;
    @Resource
    AdvisoryService advisoryService;
    @Resource
    OtherFeeService otherFeeService;


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
    public void updatePlan(Plan plan) throws Exception {
        updatePlanInfo(plan);
        // 更新和预算明细的关联关系
        planAndPlanItemService.updatePlanAndItemRelationByPlan(plan);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePlanInfo(Plan plan) throws Exception {
        Plan oldPlan = planMapper.getPlanById(plan.getId());
        Double used = oldPlan.getFunding() - oldPlan.getUnfulfilledQuota();
        if (plan.getFunding() < used) {
            throw new Exception("总额度小于已执行额度！");
        }
        Project project = projectMapper.getProjectById(plan.getProjectId());
        Double inPlan = planMapper.getTotalByProjectId(plan.getProjectId(), plan.getId());
        if (plan.getFunding() + inPlan > project.getFunding()) {
            throw new Exception("金额过大，超出项目剩余额度！");
        }
        // 如果总额有更新, 则更新相关数据
        if (!oldPlan.getFunding().equals(plan.getFunding())) {
            Double change = plan.getFunding() - oldPlan.getFunding();
            double quota = plan.getUnfulfilledQuota() + change;
            plan.setUnfulfilledQuota(quota);
            plan.setExecuteRate(ComputeUtils.computeExecuteRate(plan.getFunding(), quota));
        }
        planMapper.updatePlan(plan);
    }

    public void updatePlanFunding(Long planId, Double change) throws Exception {
        Plan plan = planMapper.getPlanById(planId);
        if (plan == null) {
            return;
        }
        // 新建一个计划实例，解决mybatis缓存导致的问题
        Plan newPlan = new Plan();
        newPlan.setFunding(plan.getFunding() + change);
        newPlan.setId(plan.getId());
        newPlan.setProjectId(plan.getProjectId());
        newPlan.setUnfulfilledQuota(plan.getUnfulfilledQuota());
        updatePlanInfo(newPlan);
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
    @Transactional(rollbackFor = Exception.class)
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

            // 获取材料费表单
            PlanItemData materialFeeData = getMaterialFeeFeeData(workbook);
            planItemDataMap.put("materialFeeData", materialFeeData);
            total += materialFeeData.getFunding();

            // 获取测试化验加工费表单
            PlanItemData testingFeeData = getTestingFeeData(workbook);
            planItemDataMap.put("testingFeeData", testingFeeData);
            total += testingFeeData.getFunding();

            // 获取会议费表单
            PlanItemData meetingFeeData = getMeetingFeeData(workbook);
            planItemDataMap.put("meetingFeeData", meetingFeeData);
            total += meetingFeeData.getFunding();

            // 获取课题调研差旅费表单
            PlanItemData researchFeeData = getResearchFeeData(workbook);
            planItemDataMap.put("researchFeeData", researchFeeData);
            total += researchFeeData.getFunding();

            // 获取学术会议差旅费表单
            PlanItemData academicFeeData = getAcademicFeeData(workbook);
            planItemDataMap.put("academicFeeData", academicFeeData);
            total += academicFeeData.getFunding();

            // 获取北京市内交通费表单
            PlanItemData transFeeData = getTransFeeData(workbook);
            planItemDataMap.put("transFeeData", transFeeData);
            total += transFeeData.getFunding();

            // 获取出版/文献/信息传播/知识产权事务费表单
            PlanItemData publishFeeData = getPublishFeeData(workbook);
            planItemDataMap.put("publishFeeData", publishFeeData);
            total += publishFeeData.getFunding();

            // 获取劳务费表单
            PlanItemData paymentData = getPaymentData(workbook);
            planItemDataMap.put("paymentData", paymentData);
            total += paymentData.getFunding();

            // 获取专家咨询费表单
            PlanItemData advisoryData = getAdvisoryData(workbook);
            planItemDataMap.put("advisoryData", advisoryData);
            total += advisoryData.getFunding();

            // 获取设备费表单
            PlanItemData otherFeeData = getOtherFeeData(workbook);
            planItemDataMap.put("otherFeeData", otherFeeData);
            total += otherFeeData.getFunding();

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

        for (int k = 0; k < 4; k++) {
            HSSFCell cell = row.getCell(k);
            if (cell == null) {
                continue;
            }
            switch (cell.getCellType()) {
                case STRING:
                    String cellValue = cell.getStringCellValue();
                    switch (k) {
                        case 0:
                            plan.setPlanName(cellValue);
                            break;
                        case 2:
                            plan.setProjectName(cellValue);
                            break;
                        case 3:
                            plan.setRemark(cellValue);
                            break;
                    }
                    break;
                default: {
                    switch (k) {
                        case 1:
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
        Double total = 0D;
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

            equipmentFee = new EquipmentFee();
            for (int k = 0; k < 5; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                equipmentFee.setEquipmentName(cellValue);
                                break;
                            case 1:
                                equipmentFee.setUsage(cellValue);
                                break;
                            case 4:
                                equipmentFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 2:
                                equipmentFee.setAmount((int) cell.getNumericCellValue());
                                break;
                            case 3:
                                equipmentFee.setUnitPrice(cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            equipmentFeeService.addEquipmentFee(equipmentFee);
            total += equipmentFee.getSubTotal();
            list.add(equipmentFee);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getMaterialFeeFeeData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        MaterialFee materialFee = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(2);
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

            materialFee = new MaterialFee();
            for (int k = 0; k < 8; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                materialFee.setMaterialName(cellValue);
                                break;
                            case 1:
                                materialFee.setSpecification(cellValue);
                                break;
                            case 2:
                                materialFee.setUsage(cellValue);
                                break;
                            case 5:
                                materialFee.setUnit(cellValue);
                                break;
                            case 7:
                                materialFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 3:
                                materialFee.setAmount((int) cell.getNumericCellValue());
                                break;
                            case 4:
                                materialFee.setUnitPrice(cell.getNumericCellValue());
                                break;
                            case 6:
                                materialFee.setSubTotal(cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            materialFeeService.addMaterialFee(materialFee);
            total += materialFee.getSubTotal();
            list.add(materialFee);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getTestingFeeData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        TestingFee testingFee = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(3);
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

            testingFee = new TestingFee();
            for (int k = 0; k < 4; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                testingFee.setContent(cellValue);
                                break;
                            case 3:
                                testingFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 1:
                                testingFee.setTimes((int) cell.getNumericCellValue());
                                break;
                            case 2:
                                testingFee.setUnitPrice(cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            testingFeeService.addTestingFee(testingFee);
            total += testingFee.getSubTotal();
            list.add(testingFee);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getMeetingFeeData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        MeetingFee meetingFee = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(4);
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

            meetingFee = new MeetingFee();
            for (int k = 0; k < 8; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                meetingFee.setContent(cellValue);
                                break;
                            case 1:
                                meetingFee.setRelation(cellValue);
                                break;
                            case 7:
                                meetingFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 2:
                                meetingFee.setMeal(cell.getNumericCellValue());
                                break;
                            case 3:
                                meetingFee.setOtherCost(cell.getNumericCellValue());
                                break;
                            case 4:
                                meetingFee.setDays((int) cell.getNumericCellValue());
                                break;
                            case 5:
                                meetingFee.setAmount((int) cell.getNumericCellValue());
                                break;
                            case 6:
                                meetingFee.setMeetingNum((int) cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            meetingFeeService.addMeetingFee(meetingFee);
            total += meetingFee.getSubTotal();
            list.add(meetingFee);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getResearchFeeData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        ResearchFee researchFee = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(5);
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

            researchFee = new ResearchFee();
            for (int k = 0; k < 9; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                researchFee.setPlace(cellValue);
                                break;
                            case 1:
                                researchFee.setPurpose(cellValue);
                                break;
                            case 2:
                                researchFee.setPersonnelType(cellValue);
                                break;
                            case 8:
                                researchFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 3:
                                researchFee.setTimes((int) cell.getNumericCellValue());
                                break;
                            case 4:
                                researchFee.setTransFee(cell.getNumericCellValue());
                                break;
                            case 5:
                                researchFee.setAccommodationFee(cell.getNumericCellValue());
                                break;
                            case 6:
                                researchFee.setOtherFee(cell.getNumericCellValue());
                                break;
                            case 7:
                                researchFee.setDays((int) cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            researchFeeService.addResearchFee(researchFee);
            total += researchFee.getSubTotal();
            list.add(researchFee);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getAcademicFeeData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        AcademicFee academicFee = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(6);
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

            academicFee = new AcademicFee();
            for (int k = 0; k < 10; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                academicFee.setMeetingName(cellValue);
                                break;
                            case 1:
                                academicFee.setPersonnelType(cellValue);
                                break;
                            case 9:
                                academicFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 2:
                                academicFee.setTimes((int) cell.getNumericCellValue());
                                break;
                            case 3:
                                academicFee.setYear((int) cell.getNumericCellValue());
                                break;
                            case 4:
                                academicFee.setDays((int) cell.getNumericCellValue());
                                break;
                            case 5:
                                academicFee.setRegistrationFee(cell.getNumericCellValue());
                                break;
                            case 6:
                                academicFee.setTransFee(cell.getNumericCellValue());
                                break;
                            case 7:
                                academicFee.setAccommodationFee(cell.getNumericCellValue());
                                break;
                            case 8:
                                academicFee.setSubTotal(cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            academicFeeService.addAcademicFee(academicFee);
            total += academicFee.getSubTotal();
            list.add(academicFee);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getTransFeeData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        PlanItem transFee = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(7);
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

            transFee = new PlanItem();
            for (int k = 0; k < 2; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 1:
                                transFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 0:
                                transFee.setSubTotal(cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            transportationFeeService.addTransportationFee(transFee);
            total += transFee.getSubTotal();
            list.add(transFee);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getPublishFeeData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        PublishFee publishFee = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(8);
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

            publishFee = new PublishFee();
            for (int k = 0; k < 3; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                publishFee.setType(cellValue);
                                break;
                            case 2:
                                publishFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 1:
                                publishFee.setSubTotal(cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            publishFeeService.addPublishFee(publishFee);
            total += publishFee.getSubTotal();
            list.add(publishFee);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getPaymentData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        Payment payment = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(9);
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

            payment = new Payment();
            for (int k = 0; k < 6; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                payment.setPersonnelType(cellValue);
                                break;
                            case 1:
                                payment.setContent(cellValue);
                                break;
                            case 5:
                                payment.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 2:
                                payment.setAmount((int) cell.getNumericCellValue());
                                break;
                            case 3:
                                payment.setStandard(cell.getNumericCellValue());
                                break;
                            case 4:
                                payment.setHours((int) cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            paymentService.addPayment(payment);
            total += payment.getSubTotal();
            list.add(payment);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getAdvisoryData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        Advisory advisory = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(10);
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

            advisory = new Advisory();
            for (int k = 0; k < 6; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                advisory.setPersonnelType(cellValue);
                                break;
                            case 1:
                                advisory.setContent(cellValue);
                                break;
                            case 5:
                                advisory.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 2:
                                advisory.setAmount((int) cell.getNumericCellValue());
                                break;
                            case 3:
                                advisory.setStandard(cell.getNumericCellValue());
                                break;
                            case 4:
                                advisory.setDays((int) cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            advisoryService.addAdvisory(advisory);
            total += advisory.getSubTotal();
            list.add(advisory);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

    private PlanItemData getOtherFeeData(HSSFWorkbook workbook) {
        PlanItemData data = new PlanItemData();
        List<PlanItem> list = new ArrayList<>();
        OtherFee otherFee = null;
        Double total = 0D;
        HSSFSheet sheet = workbook.getSheetAt(11);
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

            otherFee = new OtherFee();
            for (int k = 0; k < 3; k++) {
                HSSFCell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 0:
                                otherFee.setType(cellValue);
                                break;
                            case 2:
                                otherFee.setRemark(cellValue);
                                break;
                        }
                        break;
                    default: {
                        switch (k) {
                            case 1:
                                otherFee.setSubTotal(cell.getNumericCellValue());
                                break;
                        }
                    }
                    break;
                }
            }
            otherFeeService.addOtherFee(otherFee);
            total += otherFee.getSubTotal();
            list.add(otherFee);
        }
        data.setPlanItemList(list);
        data.setFunding(total);
        return data;
    }

}
