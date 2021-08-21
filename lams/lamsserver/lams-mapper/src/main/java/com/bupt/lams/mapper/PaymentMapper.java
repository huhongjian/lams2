package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.Payment;

import java.util.List;

public interface PaymentMapper {
    int insertSelective(Payment payment);

    List<Payment> getPaymentByIds(List<Long> ids);

    /**
     * 根据ids获取完整信息，用于执行预算明细
     */
    List<Payment> getFullInfoByIds(List<Long> ids);

    Payment getById(Long id);

    void update(Payment payment);

    void clear(String userEmail);

    List<Payment> getAll();
}
