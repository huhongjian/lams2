package com.bupt.lams.mapper;

import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AssetMapper {
    List<Asset> getAssetByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("asset") Asset asset, @Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("asset") Asset asset, @Param("beginDateScope") Date[] beginDateScope);
}