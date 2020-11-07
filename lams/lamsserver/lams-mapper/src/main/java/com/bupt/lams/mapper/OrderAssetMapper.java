package com.bupt.lams.mapper;

import com.bupt.lams.model.OrderAsset;

import java.util.List;

public interface OrderAssetMapper {
    int insertSelective(OrderAsset orderAsset);

    Long getAidByOid(Long oid);

    /**
     * 根据资产编号获取最近的一个工单id
     *
     * @param aid
     * @return
     */
    Long getLatestOidByAid(Long aid);

    void deleteManyByOids(List<Long> oids);
}