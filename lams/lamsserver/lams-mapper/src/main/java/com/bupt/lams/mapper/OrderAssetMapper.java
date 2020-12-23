package com.bupt.lams.mapper;

import com.bupt.lams.model.OrderAsset;
import org.apache.ibatis.annotations.Param;

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

    void deleteManyByAids(@Param("aids") List<Long> aids, @Param("oid") Long oid);
}