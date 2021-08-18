package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.Payment;

import java.util.List;

public interface PaymentMapper {
    int insertSelective(Payment payment);

    List<Payment> getPaymentByIds(List<Long> ids);

    Payment getById(Long id);

    void update(Payment payment);

    void clear(String userEmail);
}
