package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.AcademicFee;
import com.bupt.lams.model.planItem.TestingFee;
import com.bupt.lams.service.TestingFeeService;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试化验加工费相关
 */
@RestController
@RequestMapping("/planItem/testingFee")
public class TestingFeeController {
    private Logger logger = LoggerFactory.getLogger(TestingFeeController.class);

    @Resource
    TestingFeeService testingFeeService;

    @PostMapping("/add")
    public RespBean addTestingFee(@RequestBody TestingFee testingFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (testingFee == null) {
            logger.error("测试化验加工费不能为空！");
            return RespBean.error("测试化验加工费不能为空！");
        }
        if (testingFee.getUnitPrice() == null) {
            logger.error("单价不能为空！");
            return RespBean.error("单价不能为空！");
        }
        if (testingFee.getTimes() == null) {
            logger.error("次数不能为空！");
            return RespBean.error("次数不能为空！");
        }
        try {
            Long id = testingFeeService.addTestingFee(testingFee);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增测试化验加工费失败", e);
            return RespBean.error("新增测试化验加工费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getTestingFeeByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            response.setObj(new PlanItemData());
            return response;
        }
        try {
            PlanItemData data = testingFeeService.getTestingFeeDataByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取测试化验加工费信息失败", e);
            return RespBean.error("获取测试化验加工费信息失败！");
        }
        return response;
    }

    @DeleteMapping("/clear")
    public RespBean clear() {
        try {
            testingFeeService.clear();
        } catch (Exception e) {
            return RespBean.error("清空无效预算明细失败!");
        }
        return RespBean.ok(null);
    }
}
