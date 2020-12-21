package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Order;
import com.bupt.lams.service.OrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 新资产申请被拒绝
 */
@Component
public class HandleReject implements IUpdateStatus {
    @Resource
    OrderService orderService;
    @Resource
    AssetMapper assetMapper;

    @Override
    public void updateStage(TaskHandleDto taskHandleDto, Order order) {
        order.setStatus(OrderStatusEnum.REJECTED.getIndex());
        orderService.updateOrderStatusById(order);
        order = orderService.selectFullOrderInfoById(order.getId());
        List<Asset> assetList = order.getAssetList();
        for (Asset asset : assetList) {
            asset.setStatus(AssetStatusEnum.REJECTED.getIndex());
            assetMapper.updateAssetStatus(asset);
        }
    }
}
