package com.bupt.lams.service;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.AssetTypeEnum;
import com.bupt.lams.dto.AssetDashBoardHeadTableData;
import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.AssetStatusCount;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.RespPageBean;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    public void changeAssetStatus(Asset asset) {
        asset.setStatus(asset.getStatus());
        assetMapper.updateAssetStatus(asset);
    }

    public AssetDashBoardHeadTableData getHeadTableData() {
        AssetDashBoardHeadTableData data = new AssetDashBoardHeadTableData();
        // 获取总数
        AssetQueryCondition condition = new AssetQueryCondition();
        List<Integer> statusList = new ArrayList<>();
        statusList.add(AssetStatusEnum.FREE.getIndex());
        statusList.add(AssetStatusEnum.BROKEN.getIndex());
        statusList.add(AssetStatusEnum.INUSE.getIndex());
        condition.setAssetStatuses(statusList);
        Long total = assetMapper.getTotalByCondition(condition);
        data.setTotal(total);
        // 获取总金额
        data.setMoney(assetMapper.getAliveAssetTotalMoney());
        // 获取其他数据
        List<AssetStatusCount> assetStatusCounts = assetMapper.getAssetStatusCount(null);
        if (CollectionUtils.isNotEmpty(assetStatusCounts)) {
            for (AssetStatusCount ac : assetStatusCounts) {
                Integer status = ac.getStatus();
                Long count = ac.getCount();
                switch (status) {
                    case 2:
                        data.setFree(count);
                        break;
                    case 3:
                        data.setInRepair(count);
                        break;
                    case 4:
                        data.setCleaned(count);
                        break;
                    case 6:
                        data.setInUse(count);
                        break;
                }
            }
        }
        return data;
    }

    public List<AssetStatusCount> getRingData(List<String> typeList) {
        return assetMapper.getAssetStatusCount(typeList);
    }
}
