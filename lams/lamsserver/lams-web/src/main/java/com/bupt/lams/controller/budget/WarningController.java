package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.WarningConfigQueryCondition;
import com.bupt.lams.dto.WarningRecordQueryCondition;
import com.bupt.lams.model.*;
import com.bupt.lams.service.MessageContentService;
import com.bupt.lams.service.WarningConfigService;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 预算报警相关
 */
@RestController
@RequestMapping("/warning")
public class WarningController {
    private Logger logger = LoggerFactory.getLogger(WarningController.class);

    @Resource
    WarningConfigService warningConfigService;
    @Resource
    MessageContentService messageContentService;

    @GetMapping("/config/get")
    public RespPageBean getWarningConfigByPage(WarningConfigQueryCondition executionQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            executionQueryCondition.setStartDate(dateScope[0]);
            executionQueryCondition.setEndDate(dateScope[1]);
        }
        return warningConfigService.getWarningConfigByCondition(executionQueryCondition);
    }

    @GetMapping("/record/get")
    public RespPageBean getWarningRecordByPage(WarningRecordQueryCondition warningRecordQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            warningRecordQueryCondition.setStartDate(dateScope[0]);
            warningRecordQueryCondition.setEndDate(dateScope[1]);
        }
        return messageContentService.getMessageContentByCondition(warningRecordQueryCondition);
    }

    @PostMapping("/config/add")
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

    @DeleteMapping("/config/delete")
    public RespBean deleteConfigs(@RequestBody List<Long> ids) {
        // 只允许教师删除
        if (UserInfoUtils.isTeacher() == false) {
            return RespBean.error("您没有删除权限，请联系管理员!");
        }
        try {
            warningConfigService.deleteConfigs(ids);
        } catch (Exception e) {
            return RespBean.error("删除失败!");
        }
        return RespBean.ok("删除成功!");
    }

    @PutMapping("/config/edit")
    public RespBean updateConfig(@RequestBody WarningConfig config) {
        try {
            warningConfigService.update(config);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }

    @PutMapping("/config/pause")
    public RespBean pauseConfig(@RequestBody List<Long> ids) {
        try {
            warningConfigService.pauseConfig(ids);
        } catch (Exception e) {
            return RespBean.error("暂停失败!");
        }
        return RespBean.ok("暂停成功!");
    }

    @PutMapping("/config/start")
    public RespBean startConfig(@RequestBody List<Long> ids) {
        try {
            warningConfigService.startConfig(ids);
        } catch (Exception e) {
            return RespBean.error("启动失败!");
        }
        return RespBean.ok("启动成功!");
    }
}
