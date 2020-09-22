package com.bupt.lams.mapper;

import com.bupt.lams.model.Asset;
import com.bupt.lams.model.AssetIn;
import com.bupt.lams.model.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AssetInMapper {
    int insertSelective(AssetIn assetIn);

    List<Asset> getAssetInByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("assetIn") AssetIn assetIn, @Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("assetIn") AssetIn assetIn, @Param("beginDateScope") Date[] beginDateScope);
}