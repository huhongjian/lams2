package com.bupt.lams.mapper;

import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.AssetStatusCount;
import com.bupt.lams.model.Asset;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AssetMapper {
    /**
     * 根据条件获取资产信息
     *
     * @param condition
     * @return
     */
    List<Asset> getAssetByCondition(AssetQueryCondition condition);

    /**
     * 获取符合条件的资产总数
     *
     * @param condition
     * @return
     */
    Long getTotalByCondition(AssetQueryCondition condition);

    void updateAssetStatus(Asset asset);

    void updateAsset(Asset asset);

    int insertSelective(Asset assetIn);

    Asset getAssetById(Long aid);

    List<AssetStatusCount> getAssetStatusCount(List<String> typeList);

    List<AssetStatusCount> getAssetTypeCount();

    List<AssetStatusCount> getLineData(Date[] monthScope);

    AssetStatusCount getTotalLineData(Date[] monthScope);

    /**
     * sum函数有精度问题，这里对结果保留两位小数
     *
     * @return
     */
    Double getAliveAssetTotalMoney();

    List<Long> getAllAssetIds();

    List<Asset> getAssetInfoByIds(List<Long> aids);

    void deleteManyByAids(List<Long> aids);

    void clearAssets();
}