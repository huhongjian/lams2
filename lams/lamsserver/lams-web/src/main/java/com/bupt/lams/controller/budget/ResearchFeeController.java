package com.bupt.lams.controller.budget;

import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.AcademicFee;
import com.bupt.lams.model.planItem.ResearchFee;
import com.bupt.lams.service.ResearchFeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课题调研差旅费相关
 */
@RestController
@RequestMapping("/planItem/researchFee")
public class ResearchFeeController {
    private Logger logger = LoggerFactory.getLogger(ResearchFeeController.class);

    @Resource
    ResearchFeeService researchFeeService;

    @GetMapping("/get")
    public RespBean getResearchFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<ResearchFee> data = researchFeeService.getResearchFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取课题调研差旅费失败", e);
            return RespBean.error("获取课题调研差旅费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addResearchFee(@RequestBody ResearchFee researchFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (researchFee == null) {
            logger.error("课题调研差旅费不能为空！");
            return RespBean.error("课题调研差旅费不能为空！");
        }
        try {
            Long id = researchFeeService.addResearchFee(researchFee);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增课题调研差旅费失败", e);
            return RespBean.error("新增课题调研差旅费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getResearchFeeByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            return response;
        }
        try {
            List<ResearchFee> data = researchFeeService.getResearchFeeListByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取课题调研差旅费信息失败", e);
            return RespBean.error("获取课题调研差旅费信息失败！");
        }
        return response;
    }
}
