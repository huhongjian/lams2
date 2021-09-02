package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanQueryCondition;
import com.bupt.lams.model.Plan;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.PlanService;
import com.bupt.lams.service.ProjectService;
import com.bupt.lams.utils.POIUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 预算计划相关
 */
@RestController
@RequestMapping("/plan")
public class PlanController {
    private Logger logger = LoggerFactory.getLogger(PlanController.class);

    @Resource
    PlanService planService;
    @Resource
    ProjectService projectService;

    @GetMapping("/get")
    public RespPageBean getPlanInfoByPage(PlanQueryCondition planQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            planQueryCondition.setStartDate(dateScope[0]);
            planQueryCondition.setEndDate(dateScope[1]);
        }
        return planService.getPlanByCondition(planQueryCondition);
    }

    @GetMapping("/get/all")
    public RespBean getAllPlan() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<Plan> data = planService.getAllPlans();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取预算计划列表失败", e);
            return RespBean.error("获取预算计划列表失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addPlan(@RequestBody Plan plan) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("预算计划新增成功!");
        try {
            Project project = projectService.getProjectById(plan.getProjectId());
            Double inPlan = projectService.getInPlanById(plan.getProjectId(), null);
            if (plan.getFunding() + inPlan > project.getFunding()) {
                return RespBean.error("新增项目失败！金额过大，超出项目剩余额度！");
            }
            planService.addPlan(plan);
        } catch (Exception e) {
            logger.error("新增项目失败", e);
            return RespBean.error("新增项目失败！");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean updatePlan(@RequestBody Plan plan) {
        try {
            planService.updatePlan(plan);
        } catch (Exception e) {
            return RespBean.error("更新失败!" + e.getMessage());
        }
        return RespBean.ok("更新成功!");
    }

    @DeleteMapping("/delete")
    public RespBean deletePlans(@RequestBody List<Long> pids) {
        // 只允许教师删除
        if (UserInfoUtils.isTeacher() == false) {
            return RespBean.error("您没有删除权限，请联系管理员!");
        }
        try {
            planService.deletePlans(pids);
        } catch (Exception e) {
            return RespBean.error("删除失败!");
        }
        return RespBean.ok("删除成功!");
    }

    @PostMapping("/import")
    public RespBean importData(MultipartFile file) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("上传成功!");
        try {
            Plan plan = planService.excel2Plan(file);
            planService.addPlan(plan);
        } catch (Exception e) {
            logger.error("上传失败", e);
            return RespBean.error("上传失败！");
        }
        return response;
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(Long id) throws Exception {
        PlanQueryCondition condition = new PlanQueryCondition();
        condition.setPlanId(id);
        List<Plan> planList = planService.getFullPlanInfo(condition);
        if (planList.size() != 1) {
            return null;
        }
        return POIUtils.planIn2Excel(planList.get(0));
    }

    @GetMapping("/template")
    public ResponseEntity<byte[]> getTemplate() {
        return POIUtils.planExcelTemplate();
    }
}
