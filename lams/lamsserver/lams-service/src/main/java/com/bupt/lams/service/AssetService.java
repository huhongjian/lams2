package com.bupt.lams.service;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.AssetDashBoardData;
import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.AssetStatusCount;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.mapper.AssetPicsMapper;
import com.bupt.lams.mapper.OrderAssetMapper;
import com.bupt.lams.mapper.OrderMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.service.annotation.OperateRecord;
import com.bupt.lams.service.strategies.record.ChangeAssetStatusRecord;
import com.bupt.lams.service.strategies.record.UpdateAssetRecord;
import com.bupt.lams.utils.FastDFSUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 资产service
 */
@Service
public class AssetService {
    @Resource
    AssetMapper assetMapper;

    @Resource
    AssetPicsMapper assetPicsMapper;

    @Value("${fastdfs.nginx.host}")
    String nginxHost;

    @Resource
    OrderAssetMapper orderAssetMapper;

    @Resource
    OrderMapper orderMapper;

    public RespPageBean getAssetByCondition(AssetQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Asset> data = assetMapper.getAssetByCondition(condition);
        return transToRespPageBean(page, size, data);
    }

    public RespPageBean getAvailable(AssetQueryCondition condition) {
        // 需要是闲置状态
        List<Integer> assetStatuses = new ArrayList<>();
        assetStatuses.add(AssetStatusEnum.FREE.getIndex());
        condition.setAssetStatuses(assetStatuses);
        // 并且没有被其他工单占用
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        // 所有满足查找条件的资产信息
        List<Asset> data = assetMapper.getAssetByCondition(condition);
        // 可以借用的资产信息
        List<Asset> availableList = new ArrayList<>();
        for (Asset asset : data) {
            if (isAvailable(asset.getId()) == true) {
                availableList.add(asset);
            }
        }
        return transToRespPageBean(page, size, availableList);
    }

    public RespPageBean getReturn(AssetQueryCondition condition) {
        // 需要是使用中状态
        List<Integer> assetStatuses = new ArrayList<>();
        assetStatuses.add(AssetStatusEnum.INUSE.getIndex());
        condition.setAssetStatuses(assetStatuses);
        // 是我借用的
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        condition.setPage(null);
        condition.setSize(null);
        // 所有满足查找条件的资产信息
        List<Asset> data = assetMapper.getAssetByCondition(condition);
        // 可以借用的资产信息
        List<Asset> returnList = new ArrayList<>();
        for (Asset asset : data) {
            if (isReturn(asset.getId()) == true) {
                returnList.add(asset);
            }
        }
        return transToRespPageBean(page, size, returnList);
    }

    @OperateRecord(description = "更新资产信息", clazz = UpdateAssetRecord.class)
    public void updateAsset(Asset asset) {
        assetMapper.updateAsset(asset);
    }

    @OperateRecord(description = "修改资产状态", clazz = ChangeAssetStatusRecord.class)
    public void changeAssetStatus(Asset asset) {
        // 获取中文名
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

    public void insertAssetPics(AssetPic pic) {
        assetPicsMapper.insertAssetPics(pic);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAssetPicById(Long id) throws IOException, MyException {
        AssetPic assetPic = assetPicsMapper.selectByPrimaryKey(id);
        if (assetPic == null) {
            return;
        }
        String fileId = assetPic.getUrl().replace(nginxHost, "");
        FastDFSUtils.delete(fileId);
        assetPicsMapper.deleteAssetPicById(id);
    }

    public List<Long> getAllAssetIds() {
        return assetMapper.getAllAssetIds();
    }

    public List<Asset> getAssetInfoByIds(List<Long> aids) {
        return assetMapper.getAssetInfoByIds(aids);
    }

    public RespPageBean getAssetPageInfoByIds(AssetQueryCondition initData) {
        List<Long> aids = initData.getAids();
        Integer page = initData.getPage();
        Integer size = initData.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        // 所有满足查找条件的资产信息
        List<Asset> data = assetMapper.getAssetInfoByIds(aids);
        return transToRespPageBean(page, size, data);
    }

    /**
     * 将结果集转化为展示集合
     *
     * @param page
     * @param size
     * @param data
     * @return
     */
    private RespPageBean transToRespPageBean(Integer page, Integer size, List<Asset> data) {
        Long total = Long.valueOf(data.size());
        Integer end = Math.min(page + size, data.size());
        // 展示的数据
        List<Asset> res = new ArrayList<>();
        for (Integer i = page; i < end; i++) {
            res.add(data.get(i));
        }
        RespPageBean bean = new RespPageBean();
        bean.setData(res);
        bean.setTotal(total);
        return bean;
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

    @Transactional(rollbackFor = Exception.class)
    public List<Long> addAsset(Asset asset, Integer amount) {
        List<Long> aids = new ArrayList<>();
        for (Integer i = 0; i < amount; i++) {
            asset.setStatus(AssetStatusEnum.CREATE.getIndex());
            assetMapper.insertSelective(asset);
            aids.add(asset.getId());
        }
        return aids;
    }

    /**
     * 清空多余的资产信息，即，没有和采购单关联的资产信息
     */
    public void clearAssets() {
        assetMapper.clearAssets();
    }

    public void addAssetOrderRelation(List<Long> assetIds, Long oid) {
        for (Long assetId : assetIds) {
            OrderAsset orderAsset = new OrderAsset();
            orderAsset.setAid(assetId);
            orderAsset.setOid(oid);
            orderAsset.setCreateTime(new Date());
            orderAsset.setUpdateTime(new Date());
            orderAssetMapper.insertSelective(orderAsset);
        }
    }

    /**
     * 是否可以借用
     * 如果资产是闲置，并且没有被出库工单占用，就可以借用
     *
     * @param aid
     * @return
     */
    private Boolean isAvailable(Long aid) {
        Long oid = orderAssetMapper.getLatestOidByAid(aid);
        Order order = orderMapper.selectBaseOrderInfoById(oid);
        if (order.getStatus() == OrderStatusEnum.ASK.getIndex()) {
            return false;
        }
        return true;
    }

    /**
     * 是我借用的
     * 如果资产是使用中，并且最近一个工单是借用工单，申请人是我
     *
     * @param aid
     * @return
     */
    private Boolean isReturn(Long aid) {
        Long oid = orderAssetMapper.getLatestOidByAid(aid);
        Order order = orderMapper.selectBaseOrderInfoById(oid);
        LamsUser user = UserInfoUtils.getLoginedUser();
        if (order.getStatus() == OrderStatusEnum.OCCUPIED.getIndex() && order.getUserEmail().equals(user.getUsername())) {
            return true;
        }
        return false;
    }
}
