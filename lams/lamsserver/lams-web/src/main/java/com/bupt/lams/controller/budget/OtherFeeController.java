package com.bupt.lams.controller.budget;

import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.OtherFee;
import com.bupt.lams.service.OtherFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 其他支出相关
 */
@RestController
@RequestMapping("/planItem/otherFee")
public class OtherFeeController {
    private Logger logger = LoggerFactory.getLogger(OtherFeeController.class);

    @Resource
    OtherFeeService otherFeeService;

    @GetMapping("/get")
    public RespBean getOtherFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<OtherFee> data = otherFeeService.getOtherFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取其他支出失败", e);
            return RespBean.error("获取其他支出失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addOtherFee(@RequestBody OtherFee otherFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (otherFee == null) {
            logger.error("其他支出不能为空！");
            return RespBean.error("其他支出不能为空！");
        }
        try {
            otherFeeService.addOtherFee(otherFee);
        } catch (Exception e) {
            logger.error("新增其他支出失败", e);
            return RespBean.error("新增其他支出失败！");
        }
        return response;
    }
}
