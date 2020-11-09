package com.bupt.lams.mapper;

import com.bupt.lams.dto.PurchaseQueryCondition;
import com.bupt.lams.model.PurchaseOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchaseAssetMapper {
    List<PurchaseOrder> getPurchaseOrderByCondition(PurchaseQueryCondition condition);

    Long getTotalByCondition(PurchaseQueryCondition condition);

    PurchaseOrder selectByPrimaryKey(Long id);

    void deleteByPoid(@Param("poid") Long poid);

    void insertSelective(@Param("poid") Long poid, @Param("aids") List<Long> aids);

    List<Long> getPurchaseOrderIdsByAids(@Param("aids") List<Long> aids);

    void deleteManyByIds(List<Long> poids);
}
