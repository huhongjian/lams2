package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.ExecutionQueryCondition;
import com.bupt.lams.model.BudgetRecord;
import com.bupt.lams.model.Plan;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.model.planItem.PlanItem;
import com.bupt.lams.service.BudgetRecordService;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 预算执行流水相关
 */
@RestController
@RequestMapping("/execution")
public class ExecutionController {
    private Logger logger = LoggerFactory.getLogger(ExecutionController.class);

    @Resource
    BudgetRecordService budgetRecordService;

    @GetMapping("/get")
    public RespPageBean getBudgetRecordByPage(ExecutionQueryCondition executionQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            executionQueryCondition.setStartDate(dateScope[0]);
            executionQueryCondition.setEndDate(dateScope[1]);
        }
        return budgetRecordService.getBudgetRecordByCondition(executionQueryCondition);
    }

    @PostMapping("/item/get")
    public RespBean getBudgetRecordByPlanItem(@RequestBody PlanItem item) {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<BudgetRecord> data = budgetRecordService.getBudgetRecordByPlanItem(item);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取执行记录失败", e);
            return RespBean.error("获取执行记录失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addExecution(@RequestBody BudgetRecord record) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("预算执行成功!");
        try {
            budgetRecordService.addBudgetRecord(record);
        } catch (Exception e) {
            logger.error("预算执行失败", e);
            return RespBean.error("预算执行失败！" + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/delete")
    public RespBean deleteExecution(@RequestBody List<Long> rids) {
        // 只允许教师删除
        if (UserInfoUtils.isTeacher() == false) {
            return RespBean.error("您没有撤销权限，请联系管理员!");
        }
        try {
            budgetRecordService.deleteExecution(rids);
        } catch (Exception e) {
            return RespBean.error("撤销失败!");
        }
        return RespBean.ok("撤销成功!");
    }

    @PutMapping("/reimburse")
    public RespBean reimburseRecord(@RequestBody List<Long> ids) {
        try {
            budgetRecordService.reimburseRecord(ids);
        } catch (Exception e) {
            return RespBean.error("报销失败!");
        }
        return RespBean.ok("报销成功!");
    }

    @PutMapping("/cancel")
    public RespBean cancelReimburse(@RequestBody List<Long> ids) {
        try {
            budgetRecordService.cancelReimburse(ids);
        } catch (Exception e) {
            return RespBean.error("取消报销失败!");
        }
        return RespBean.ok("取消报销成功!");
    }
}
