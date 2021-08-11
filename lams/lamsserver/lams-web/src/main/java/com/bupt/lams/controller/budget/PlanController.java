package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanQueryCondition;
import com.bupt.lams.model.Plan;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.PlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 预算计划相关
 */
@RestController
@RequestMapping("/plan")
public class PlanController {
    private Logger logger = LoggerFactory.getLogger(PlanController.class);

    @Resource
    PlanService planService;

    @GetMapping("/get")
    public RespPageBean getPlanInfoByPage(PlanQueryCondition planQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            planQueryCondition.setStartDate(dateScope[0]);
            planQueryCondition.setEndDate(dateScope[1]);
        }
        return planService.getPlanByCondition(planQueryCondition);
    }

    @PostMapping("/add")
    public RespBean addPlan(@RequestBody Plan plan) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("预算计划新增成功!");
        try {
            planService.addPlan(plan);
        } catch (Exception e) {
            logger.error("新增项目失败", e);
            return RespBean.error("新增项目失败！");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean updateProject(@RequestBody Project project) {
        try {
            planService.updateAsset(project);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }
}
