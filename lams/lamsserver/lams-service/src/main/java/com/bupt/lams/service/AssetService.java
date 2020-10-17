package com.bupt.lams.service;

import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.RespPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资产service
 */
@Service
public class AssetService {
    @Resource
    AssetMapper assetMapper;

    public RespPageBean getAssetByCondition(AssetQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        condition.setPage(page);
        List<Asset> data = assetMapper.getAssetByCondition(condition);
        Long total = assetMapper.getTotalByCondition(condition);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public void updateAsset(Asset asset) {
        assetMapper.updateAsset(asset);
    }
}
