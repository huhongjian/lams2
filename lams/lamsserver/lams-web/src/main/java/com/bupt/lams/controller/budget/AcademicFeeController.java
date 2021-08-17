package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.AcademicFee;
import com.bupt.lams.service.AcademicFeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 国内学术会议差旅费相关
 */
@RestController
@RequestMapping("/planItem/academicFee")
public class AcademicFeeController {
    private Logger logger = LoggerFactory.getLogger(AcademicFeeController.class);

    @Resource
    AcademicFeeService academicFeeService;

    @GetMapping("/get")
    public RespBean getAcademicFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<AcademicFee> data = academicFeeService.getAcademicFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取国内学术会议差旅费失败", e);
            return RespBean.error("获取国内学术会议差旅费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addAcademicFee(@RequestBody AcademicFee academicFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (academicFee == null) {
            logger.error("国内学术会议差旅费不能为空！");
            return RespBean.error("国内学术会议差旅费不能为空！");
        }
        try {
            Long id = academicFeeService.addAcademicFee(academicFee);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增国内学术会议差旅费失败", e);
            return RespBean.error("新增国内学术会议差旅费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getAcademicFeeByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            return response;
        }
        try {
            PlanItemData data = academicFeeService.getAcademicFeeListByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取国内学术会议差旅费信息失败", e);
            return RespBean.error("获取国内学术会议差旅费信息失败！");
        }
        return response;
    }
}
