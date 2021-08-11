package com.bupt.lams.mapper;

import com.bupt.lams.model.PlanAndItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanAndItemMapper {
    int insertSelective(PlanAndItem planAndItem);

    int insertMany(List<PlanAndItem> planAndItemList);

    List<PlanAndItem> getPlanAndItemListByPid(Long pid);

    void deleteManyByOids(List<Long> oids);

    void deleteManyByAids(@Param("aids") List<Long> aids, @Param("oid") Long oid);
}