package com.bupt.lams.mapper;

import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AssetMapper {
    void updateAsset(Asset asset);

    int insertSelective(Asset assetIn);
}