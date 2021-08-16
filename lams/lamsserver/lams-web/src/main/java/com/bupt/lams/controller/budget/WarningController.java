package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.ExecutionQueryCondition;
import com.bupt.lams.dto.WarningConfigQueryCondition;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.model.WarningConfig;
import com.bupt.lams.service.WarningConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 预算报警相关
 */
@RestController
@RequestMapping("/warning")
public class WarningController {
    private Logger logger = LoggerFactory.getLogger(WarningController.class);

    @Resource
    WarningConfigService warningConfigService;

    @GetMapping("/get")
    public RespPageBean getWarningConfigByPage(WarningConfigQueryCondition executionQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            executionQueryCondition.setStartDate(dateScope[0]);
            executionQueryCondition.setEndDate(dateScope[1]);
        }
        return warningConfigService.getWarningConfigByCondition(executionQueryCondition);
    }

    @PostMapping("/add")
    public RespBean addConfig(@RequestBody WarningConfig config) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("新增预算报警配置成功!");
        try {
            warningConfigService.addWarningConfig(config);
        } catch (Exception e) {
            logger.error("新增预算报警配置失败", e);
            return RespBean.error("新增预算报警配置失败！");
        }
        return response;
    }
}
