package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.Advisory;
import com.bupt.lams.service.AdvisoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 专家咨询费相关
 */
@RestController
@RequestMapping("/planItem/advisory")
public class AdvisoryController {
    private Logger logger = LoggerFactory.getLogger(AdvisoryController.class);

    @Resource
    AdvisoryService advisoryService;

    @GetMapping("/get")
    public RespBean getAdvisoryList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<Advisory> data = advisoryService.getAdvisoryList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取专家咨询费失败", e);
            return RespBean.error("获取专家咨询费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addAdvisory(@RequestBody Advisory advisory) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (advisory == null) {
            logger.error("专家咨询费不能为空！");
            return RespBean.error("专家咨询费不能为空！");
        }
        try {
            Long id = advisoryService.addAdvisory(advisory);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增专家咨询费失败", e);
            return RespBean.error("新增专家咨询费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getAdvisoryListByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            return response;
        }
        try {
            PlanItemData data = advisoryService.getAdvisoryDataByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取专家咨询费信息失败", e);
            return RespBean.error("获取专家咨询费信息失败！");
        }
        return response;
    }
}
