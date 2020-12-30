package com.bupt.lams.service;

import com.bupt.lams.mapper.OrderAssetMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.OrderAsset;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderAssetService {
    @Resource
    OrderAssetMapper orderAssetMapper;

    @Resource
    OrderService orderService;

    public Order getLatestOrderByAid(Long aid) {
        Long oid = orderAssetMapper.getLatestOidByAid(aid);
        return orderService.selectBaseOrderInfoById(oid);
    }

    /**
     * 更新工单和资产之前的关联关系
     * 删除旧绑定关系，添加新关系
     *
     * @param order
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderAssetRelation(Order order) {
        // 删除旧的
        List<Long> oids = new ArrayList<>();
        oids.add(order.getId());
        orderAssetMapper.deleteManyByOids(oids);
        // 添加新的
        List<Asset> assetList = order.getAssetList();
        List<OrderAsset> orderAssetList = new ArrayList<>();
        for (Asset asset : assetList) {
            OrderAsset orderAsset = new OrderAsset(order.getId(), asset.getId(), new Date(), new Date());
            orderAssetList.add(orderAsset);
        }
        orderAssetMapper.insertMany(orderAssetList);
    }
}
