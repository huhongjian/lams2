package com.bupt.lams.controller.budget;

import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.Advisory;
import com.bupt.lams.service.AdvisoryService;
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
            advisoryService.addAdvisory(advisory);
        } catch (Exception e) {
            logger.error("新增专家咨询费失败", e);
            return RespBean.error("新增专家咨询费失败！");
        }
        return response;
    }
}
