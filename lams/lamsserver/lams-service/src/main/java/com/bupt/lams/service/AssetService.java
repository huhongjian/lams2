package com.bupt.lams.service;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.dto.AssetDashBoardData;
import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.AssetStatusCount;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.annotation.OperateRecord;
import com.bupt.lams.service.strategies.record.ChangeAssetStatusRecord;
import com.bupt.lams.service.strategies.record.UpdateAssetRecord;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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


    @OperateRecord(description = "更新资产信息", clazz = UpdateAssetRecord.class)
    public void updateAsset(Asset asset) {
        assetMapper.updateAsset(asset);
    }

    @OperateRecord(description = "修改资产状态", clazz = ChangeAssetStatusRecord.class)
    public void changeAssetStatus(Asset asset) {
        asset.setStatus(asset.getStatus());
        assetMapper.updateAssetStatus(asset);
    }

    public AssetDashBoardData getHeadTableData() {
        AssetDashBoardData data = new AssetDashBoardData();
        // 获取总数
        data.setTotal(geAliveAssetTotal());
        // 获取总金额
        Double money = assetMapper.getAliveAssetTotalMoney();
        if (money != null) {
            data.setMoney(money);
        }
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

    public List<AssetStatusCount> getTableData(List<String> typeList) {
        List<AssetStatusCount> data = new ArrayList<>();
        // 获取总计
        AssetStatusCount totalRow = new AssetStatusCount(0, "总资产", geAliveAssetTotal(),
                assetMapper.getAliveAssetTotalMoney());
        // 获取其他数据
        List<AssetStatusCount> assetStatusCounts = assetMapper.getAssetStatusCount(typeList);
        data.add(totalRow);
        if (CollectionUtils.isNotEmpty(assetStatusCounts)) {
            data.addAll(assetStatusCounts);
        }
        return data;
    }

    public List<AssetStatusCount> getRingData(List<String> typeList) {
        return assetMapper.getAssetStatusCount(typeList);
    }

    public List<AssetStatusCount> getTypeChartData() {
        return assetMapper.getAssetTypeCount();
    }

    public List<AssetDashBoardData> getLineData(Date[] monthScope) {
        List<AssetDashBoardData> data = new ArrayList<>();
        Map<String, AssetDashBoardData> lineDataMap = new HashMap<>();
        AssetStatusCount totalStatusCount = assetMapper.getTotalLineData(monthScope);
        if (totalStatusCount == null) {
            return new ArrayList<>();
        }
        totalStatusCount.setStatus(0);
        List<AssetStatusCount> assetStatusCountList = assetMapper.getLineData(monthScope);
        if (CollectionUtils.isEmpty(assetStatusCountList)) {
            assetStatusCountList = new ArrayList<>();
        }
        assetStatusCountList.add(totalStatusCount);
        for (AssetStatusCount ac : assetStatusCountList) {
            String date = ac.getDate();
            if (lineDataMap.get(date) == null) {
                lineDataMap.put(date, new AssetDashBoardData());
            }
        }
        for (AssetStatusCount ac : assetStatusCountList) {
            String date = ac.getDate();
            Integer status = ac.getStatus();
            Long count = ac.getCount();
            switch (status) {
                case 0:
                    lineDataMap.get(date).setTotal(count);
                    break;
                case 2:
                    lineDataMap.get(date).setFree(count);
                    break;
                case 3:
                    lineDataMap.get(date).setInRepair(count);
                    break;
                case 6:
                    lineDataMap.get(date).setInUse(count);
                    break;
            }
        }
        for (String date : lineDataMap.keySet()) {
            AssetDashBoardData ad = lineDataMap.get(date);
            ad.setDate(date);
            data.add(ad);
        }
        return data;
    }

    /**
     * 获取目前有效的资产的总数（包括：闲置，故障，使用中）
     *
     * @return Long 总数
     */
    private Long geAliveAssetTotal() {
        AssetQueryCondition condition = new AssetQueryCondition();
        List<Integer> statusList = new ArrayList<>();
        statusList.add(AssetStatusEnum.FREE.getIndex());
        statusList.add(AssetStatusEnum.BROKEN.getIndex());
        statusList.add(AssetStatusEnum.INUSE.getIndex());
        condition.setAssetStatuses(statusList);
        return assetMapper.getTotalByCondition(condition);
    }
}
