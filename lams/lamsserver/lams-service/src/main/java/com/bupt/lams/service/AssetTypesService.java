package com.bupt.lams.service;

import com.bupt.lams.mapper.AssetTypeMapper;
import com.bupt.lams.model.AssetType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资产类型service
 */
@Service
public class AssetTypesService {
    @Resource
    AssetTypeMapper assetTypeMapper;

    public List<AssetType> getAllAssetTypes() {
        return assetTypeMapper.getAllAssetTypes();
    }
}
