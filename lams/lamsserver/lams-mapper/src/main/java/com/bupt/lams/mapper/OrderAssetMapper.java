package com.bupt.lams.mapper;

import com.bupt.lams.model.OrderAsset;

import java.util.List;

public interface OrderAssetMapper {
    int insertSelective(OrderAsset orderAsset);

    Long getAidByOid(Long oid);

    void deleteManyByOids(List<Integer> oids);
}