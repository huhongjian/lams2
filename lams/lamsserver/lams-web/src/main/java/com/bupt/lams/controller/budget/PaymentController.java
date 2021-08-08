package com.bupt.lams.controller.budget;

import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.Payment;
import com.bupt.lams.service.PaymentService;
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

    @GetMapping("/get")
    public RespBean getPaymentList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<Payment> data = paymentService.getPaymentList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取劳务费失败", e);
            return RespBean.error("获取劳务费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addPayment(@RequestBody Payment payment) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (payment == null) {
            logger.error("劳务费不能为空！");
            return RespBean.error("劳务费不能为空！");
        }
        try {
            paymentService.addPayment(payment);
        } catch (Exception e) {
            logger.error("新增劳务费失败", e);
            return RespBean.error("新增劳务费失败！");
        }
        return response;
    }
}
