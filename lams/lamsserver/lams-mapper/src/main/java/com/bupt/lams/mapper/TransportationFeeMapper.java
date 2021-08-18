package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.PlanItem;

import java.util.List;

public interface TransportationFeeMapper {

    List<PlanItem> getTransportationFeeList();

    int insertSelective(PlanItem transportationFee);

    List<PlanItem> getTransportationFeeByIds(List<Long> ids);

    PlanItem getById(Long id);

    void update(PlanItem equipmentFee);
}
