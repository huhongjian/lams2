package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.Payment;

import java.util.List;

public interface PaymentMapper {

    List<Payment> getPaymentList();

    int insertSelective(Payment payment);

    List<Payment> getPaymentByIds(List<Long> ids);
}
