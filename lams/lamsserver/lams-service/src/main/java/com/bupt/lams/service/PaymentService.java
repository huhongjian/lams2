package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.PaymentMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.OtherFee;
import com.bupt.lams.model.planItem.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    public List<Payment> getPaymentByPlanAndItem(List<PlanAndItem> planAndItemList) {
        List<Payment> res = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return res;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.PAYMENT.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return res;
        }
        return paymentMapper.getPaymentByIds(idList);
    }

    public List<Payment> getPaymentListByIds(List<Long> pids) {
        return paymentMapper.getPaymentByIds(pids);
    }
}
