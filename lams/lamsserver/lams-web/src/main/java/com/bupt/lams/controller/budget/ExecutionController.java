package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.ProjectQueryCondition;
import com.bupt.lams.model.BudgetRecord;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.BudgetRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 科研项目相关
 */
@RestController
@RequestMapping("/execution")
public class ExecutionController {
    private Logger logger = LoggerFactory.getLogger(ExecutionController.class);

    @Resource
    BudgetRecordService budgetRecordService;

    @GetMapping("/get")
    public RespPageBean getProjectInfoByPage(ProjectQueryCondition projectQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            projectQueryCondition.setStartDate(dateScope[0]);
            projectQueryCondition.setEndDate(dateScope[1]);
        }
        return budgetRecordService.getProjectByCondition(projectQueryCondition);
    }

    @PostMapping("/add")
    public RespBean addExecution(@RequestBody BudgetRecord record) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("预算执行成功!");
        try {
            budgetRecordService.addBudgetRecord(record);
        } catch (Exception e) {
            logger.error("新增项目失败", e);
            return RespBean.error("新增项目失败！");
        }
        return response;
    }
}
