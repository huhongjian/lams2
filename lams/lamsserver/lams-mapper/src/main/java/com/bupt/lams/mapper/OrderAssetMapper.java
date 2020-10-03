package com.bupt.lams.mapper;

import com.bupt.lams.model.OrderAsset;

public interface OrderAssetMapper {
    int insertSelective(OrderAsset orderAsset);
    Long getAidByOid(Long oid);
}