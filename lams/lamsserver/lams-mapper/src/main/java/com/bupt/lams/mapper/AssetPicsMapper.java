package com.bupt.lams.mapper;

import com.bupt.lams.model.AssetPic;

public interface AssetPicsMapper {
    void deleteAssetPicById(Long id);

    void insertAssetPics(AssetPic pics);

    AssetPic selectByPrimaryKey(Long id);
}
