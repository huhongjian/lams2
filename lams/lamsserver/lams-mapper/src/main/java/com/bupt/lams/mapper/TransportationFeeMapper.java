package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.PlanItem;

import java.util.List;

public interface TransportationFeeMapper {
    int insertSelective(PlanItem transportationFee);

    List<PlanItem> getTransportationFeeByIds(List<Long> ids);

    /**
     * 根据ids获取完整信息，用于执行预算明细
     */
    List<PlanItem> getFullInfoByIds(List<Long> ids);

    PlanItem getById(Long id);

    void update(PlanItem equipmentFee);

    void clear(String userEmail);

    List<PlanItem> getAll();
}
