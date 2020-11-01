package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.constants.ProcessTypeEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.mapper.OrderAssetMapper;
import com.bupt.lams.mapper.OrderMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.OrderAsset;
import com.bupt.lams.service.OrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 入库操作
 */
@Component
public class HandleAssetIn implements IUpdateStatus {
    @Resource
    OrderAssetMapper orderAssetMapper;
    @Resource
    OrderService orderService;
    @Resource
    OrderMapper orderMapper;
    @Resource
    AssetMapper assetMapper;

    @Override
    public void updateStage(TaskHandleDto taskHandleDto, Order order) {
        // 更新入库工单状态
        order.setStatus(OrderStatusEnum.READY.getIndex());
        orderService.updateOrderStatusById(order);
        // 新增出库工单
        Long id = taskHandleDto.getId();
        order.setCategory(ProcessTypeEnum.OUT.getIndex());
        order.setUserEmail(null);
        order.setUser(null);
        order.setReason(null);
        orderMapper.insertSelective(order);
        OrderAsset orderAsset = new OrderAsset();
        Long aid = orderAssetMapper.getAidByOid(id);
        orderAsset.setAid(aid);
        orderAsset.setOid(order.getId());
        orderAsset.setCreateTime(new Date());
        orderAsset.setUpdateTime(new Date());
        orderAssetMapper.insertSelective(orderAsset);
        // 更新资产入库时间
        Asset asset = new Asset();
        asset.setId(aid);
        asset.setStatus(AssetStatusEnum.FREE.getIndex());
        asset.setReadyDate(new Date());
        assetMapper.updateAsset(asset);
    }
}
