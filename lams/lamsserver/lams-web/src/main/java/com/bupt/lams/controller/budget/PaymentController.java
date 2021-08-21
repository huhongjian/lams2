package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.Payment;
import com.bupt.lams.service.PaymentService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 劳务费相关
 */
@RestController
@RequestMapping("/planItem/payment")
public class PaymentController {
    private Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Resource
    PaymentService paymentService;

    @PostMapping("/add")
    public RespBean addPayment(@RequestBody Payment payment) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (payment == null) {
            logger.error("劳务费不能为空！");
            return RespBean.error("劳务费不能为空！");
        }
        if (payment.getHours() == null || payment.getAmount() == null || payment.getStandard() == null) {
            logger.error("人数、工时等信息不能为空！");
            return RespBean.error("人数、工时等信息不能为空！");
        }
        try {
            Long id = paymentService.addPayment(payment);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增劳务费失败", e);
            return RespBean.error("新增劳务费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getPaymentByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            response.setObj(new PlanItemData());
            return response;
        }
        try {
            PlanItemData data = paymentService.getPaymentFeeDataByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取劳务费信息失败", e);
            return RespBean.error("获取劳务费信息失败！");
        }
        return response;
    }

    @DeleteMapping("/clear")
    public RespBean clear() {
        try {
            paymentService.clear();
        } catch (Exception e) {
            return RespBean.error("清空无效预算明细失败!");
        }
        return RespBean.ok(null);
    }

    /**
     * 获取推荐的预算明细
     */
    @GetMapping("/getRecommend")
    public RespBean getRecommend() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<Payment> data = paymentService.getRecommendFee();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取推荐劳务费信息失败", e);
            return RespBean.error("获取推荐劳务费信息失败！");
        }
        return response;
    }
}
