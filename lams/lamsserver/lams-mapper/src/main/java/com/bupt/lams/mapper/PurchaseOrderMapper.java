package com.bupt.lams.mapper;

import com.bupt.lams.dto.PurchaseQueryCondition;
import com.bupt.lams.model.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderMapper {
    List<PurchaseOrder> getPurchaseOrderByCondition(PurchaseQueryCondition condition);

    Long getTotalByCondition(PurchaseQueryCondition condition);

    PurchaseOrder selectByPrimaryKey(Long id);

    void deleteByPrimaryKey(Long id);

    void insertSelective(PurchaseOrder record);

    void updatePurchaseOrder(PurchaseOrder purchaseOrder);

    void deleteManyByIds(List<Long> poids);
}
