package com.bupt.lams.service;

import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 资产service
 */
@Service
public class AssetService {
    @Autowired
    AssetMapper assetMapper;

    public RespPageBean getAssetByPage(Integer page, Integer size, Asset asset, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Asset> data = assetMapper.getAssetByPage(page, size, asset, beginDateScope);
        Long total = assetMapper.getTotal(asset, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }
}
