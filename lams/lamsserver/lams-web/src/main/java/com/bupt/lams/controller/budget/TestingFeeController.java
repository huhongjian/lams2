package com.bupt.lams.controller.budget;

import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.TestingFee;
import com.bupt.lams.service.TestingFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预算明细相关
 */
@RestController
@RequestMapping("/planItem/testingFee")
public class TestingFeeController {
    private Logger logger = LoggerFactory.getLogger(TestingFeeController.class);

    @Resource
    TestingFeeService testingFeeService;

    @GetMapping("/get")
    public RespBean getTestingFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<TestingFee> data = testingFeeService.getTestingFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取测试化验加工费失败", e);
            return RespBean.error("获取测试化验加工费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addTestingFee(@RequestBody TestingFee testingFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (testingFee == null) {
            logger.error("测试化验加工费不能为空！");
            return RespBean.error("测试化验加工费不能为空！");
        }
        if (testingFee.getUnitPrice() == null && testingFee.getSubTotal() == null) {
            logger.error("单价和合计不能同时为空！");
            return RespBean.error("单价和合计不能同时为空！");
        }
        if (testingFee.getTimes() == null) {
            testingFee.setTimes(0);
        }
        try {
            testingFeeService.addTestingFee(testingFee);
        } catch (Exception e) {
            logger.error("新增测试化验加工费失败", e);
            return RespBean.error("新增测试化验加工费失败！");
        }
        return response;
    }
}
