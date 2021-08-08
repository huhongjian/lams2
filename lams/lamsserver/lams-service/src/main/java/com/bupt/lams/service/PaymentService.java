package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.PaymentMapper;
import com.bupt.lams.model.planItem.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试化验加工费service
 */
@Service
public class PaymentService {
    @Resource
    PaymentMapper paymentMapper;

    public List<Payment> getPaymentList() {
        return paymentMapper.getPaymentList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addPayment(Payment payment) {
        payment.setItemType(PlanItemTypeEnum.PAYMENT.getIndex());
        // 计算合计
        if (payment.getSubTotal() == null) {
            payment.setSubTotal(payment.getStandard() * payment.getHours());
        }
        paymentMapper.insertSelective(payment);
        return payment.getId();
    }
}
